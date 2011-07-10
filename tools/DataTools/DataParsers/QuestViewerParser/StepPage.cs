using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;

namespace AionQuests
{
    public partial class StepPage : UserControl
    {
        List<TableLayoutPanel> buttonPanels = new List<TableLayoutPanel>();
        List<TransparentRichTextBox> textBoxes = new List<TransparentRichTextBox>();

        public StepPage() {
            this.Dock = DockStyle.Fill;
            InitializeComponent();
            this.Disposed += new EventHandler(OnFormClose);
        }

        Quest _quest;
        Step _step;
        Step[] _allSteps;
        List<string> _collectItems = new List<string>(0);
        List<HtmlPage> _pages = new List<HtmlPage>(0);
        static IniReader _questStepMap = new IniReader();

        public StepPage(Quest quest, Step[] steps, int stepToGet) : this() {
            if (quest == null)
                throw new ArgumentNullException("quest");
            if (steps == null)
                throw new ArgumentNullException("steps");
            _allSteps = steps;
            _quest = quest;
            if (stepToGet < 1 || stepToGet > steps.Length)
                throw new ArgumentOutOfRangeException("stepToGet");
            _step = steps[stepToGet - 1];

            HtmlPage[][] sortedPages = quest.StepPages;
            if (sortedPages == null) {
                try {
                    sortedPages = quest.StepPages = GetSortedQuestPages(steps.Length);
                } catch (Exception ex) {
                    Debug.Print(ex.ToString());
                    return;
                }
            }

            if (sortedPages[stepToGet - 1] != null)
                _pages = sortedPages[stepToGet - 1].ToList();

            int row = 0;
            if (_step != null) {
                if (_step.IsCollection) {
                    if (!String.IsNullOrEmpty(_quest.collect_item1))
                        _collectItems.Add(_quest.collect_item1);
                    if (!String.IsNullOrEmpty(_quest.collect_item2))
                        _collectItems.Add(_quest.collect_item2);
                    if (!String.IsNullOrEmpty(_quest.collect_item3))
                        _collectItems.Add(_quest.collect_item3);
                    if (!String.IsNullOrEmpty(_quest.collect_item4))
                        _collectItems.Add(_quest.collect_item4);
                    foreach (string item in _collectItems) {
                        var itemLabel = new LinkLabel();
                        itemLabel.AutoSize = true;
                        itemLabel.LinkColor = Color.FromArgb(255, 255, 192);
                        itemLabel.VisitedLinkColor = Color.FromArgb(230, 230, 0);
                        itemLabel.ActiveLinkColor = Color.Yellow;
                        itemLabel.DisabledLinkColor = Color.FromArgb(224, 224, 224);
                        itemLabel.Margin = new Padding(30, 1, 3, 1);

                        string[] itemPlusCount = item.Split(' ');
                        if (itemPlusCount.Length != 2) {
                            continue;
                        }
                        itemLabel.Text = String.Format("{0} (×{1})",
                                                       Utility.ItemIndex[itemPlusCount[0]], itemPlusCount[1]);
                        tableItems.Controls.Add(itemLabel, 0, row++);
                        if (row > 1) {
                            tableItems.RowStyles.Add(new RowStyle(SizeType.AutoSize));
                        }
                        itemLabel.Links[0].LinkData = Utility.ItemIndex.GetItem(itemPlusCount[0]);
                        itemLabel.LinkClicked += new LinkLabelLinkClickedEventHandler(OnItemLinkClicked);
                        // itemLabel.Click += new EventHandler(OnLinkLabelClicked);
                    }
                    if (row != 0) {
                        lblTask.Text = Program.IniReader["collectItems"] + ':';
                        tableItems.RowCount = row;
                        tableItems.AutoSize = true;
                    } else {
                        lblTask.Text = String.Empty;
                    }
                } else {
                    lblTask.Text = String.Empty;
                }
            }

            int pageCount = 0;
            int rewardNo = 0;
            foreach (HtmlPage page in _pages) {
                pageCount++;
                if (page.name.StartsWith("select_quest_reward"))
                    rewardNo = page.HtmlPageId - 4;
                AddTabPage(page, true, rewardNo);
                //break;
            }

            tabPages.TabPages.RemoveAt(0);
        }

        HtmlPage[][] GetSortedQuestPages(int totalSteps) {
            var pages = _quest.HtmlPages.Where(p => p.name != "quest_summary" &&
                                                    p.name != "select_acqusitive_quest_desc" &&
                                                    p.name != "select_progressive_quest_desc" &&
                                                    p.name != "quest_complete" || p.ForceInclude).ToArray();
            var pagesWithReward = pages.Where(p => p != null && p.name.StartsWith("select_quest_reward"))
                                       .ToList();

            HtmlPage[][] result = new HtmlPage[totalSteps][];
            if (totalSteps == 1) { // add all pages
                result[0] = pages;
                return result;
            }

            int stepToAdd = 0;

            for (int i = 0; i < pages.Length; i++) {
                HtmlPage currentPage = pages[i];
                HtmlPage prePage = null;

                if (currentPage == null) // removed page, allready extracted
                    continue;
                bool isPrepage = currentPage.name.StartsWith("select_none") ||
                                 currentPage.name.StartsWith("ask_quest_accept") && i == 0;

                int oldStep = stepToAdd;
                bool usePrepageProcessing;
                FixQuestStep(ref stepToAdd, out usePrepageProcessing);

                if (isPrepage && usePrepageProcessing) {
                    // item touch quest if the button is also "ask_quest_accept"
                    if (currentPage.Selects != null) {
                        string actionName = currentPage.Selects[0].ActionName;
                        if (actionName != null &&
                            (actionName.StartsWith("ask_quest_accept") ||
                             actionName.StartsWith("finish_dialog"))) {
                            prePage = currentPage;
                            pages[i] = null;
                            if (pages.Length > i + 1) { // add the first select pages too
                                currentPage = pages[i + 1];
                                pages[i + 1] = null; // don't add the same
                                i++;
                            }
                        }
                    }
                }

                if (stepToAdd != 0 && _allSteps.Length > stepToAdd) {
                    // check if that (x/x) kill or gather quest; 
                    // if so, no pages for it, if the previous step wasn't a collect items quest
                    // in the latter case the dialogs have to be added
                    HtmlPage[] prevStepPages = result[stepToAdd - 1];
                    if (prevStepPages != null) {
                        bool prevHasNumbers = _allSteps[stepToAdd - 1].IsCollection ||
                                              _allSteps[stepToAdd - 1].HasCount;
                        //bool thisHasNumbers = _allSteps[stepToAdd].IsCollection ||
                        //                      _allSteps[stepToAdd].HasCount;
                        if (!prevHasNumbers && _allSteps[stepToAdd].HasCount) {
                            stepToAdd++;
                            i--; // add the same page
                            continue;
                        }
                    }
                }

                HtmlPage[] following = ExtractFollowingPages(currentPage, i, pages);
                if (following != null) {
                    if (stepToAdd > _allSteps.Length - 1) {
                        // add to the last step
                        stepToAdd = _allSteps.Length - 1;
                        int startIdx = 1;
                        if (result[stepToAdd] == null) {
                            result[stepToAdd] = new HtmlPage[following.Length + 1];
                        } else {
                            startIdx += result[stepToAdd].Length;
                            Array.Resize(ref result[stepToAdd],
                                         result[stepToAdd].Length + following.Length + 1);
                        }
                        result[stepToAdd][startIdx - 1] = currentPage;
                        Array.Copy(following, 0, result[stepToAdd], startIdx, following.Length);
                        pages[i] = null;
                        stepToAdd++; // make sure the array is always resized after
                        continue;
                    }
                    // check collection steps
                    if (_allSteps[stepToAdd].IsCollection) {
                        if (_step.Number - 1 == stepToAdd) {
                            if (result[_step.Number - 1] != null) {
                            }
                            // TODO: populate _collectItems
                        }
                    }

                    bool rewardFollows = following.Where(p => p.name.StartsWith("select_quest_reward")).Any();
                    if (rewardFollows && pagesWithReward.Count == 1 &&
                        stepToAdd < totalSteps - 1 && !_allSteps[stepToAdd].IsCollection &&
                        stepToAdd == oldStep) {
                        // only one reward and we are not on the last step,
                        // then move it forth
                        stepToAdd = totalSteps - 1;
                    }

                    int plus = prePage == null ? 0 : 1;
                    if (result[stepToAdd] == null) {
                        result[stepToAdd] = new HtmlPage[following.Length + plus + 1];
                    } else {
                        int len = result[stepToAdd].Length;
                        Array.Resize(ref result[stepToAdd], len + following.Length + plus + 1);
                        plus += len;
                    }
                    if (prePage != null)
                        result[stepToAdd][plus - 1] = prePage;
                    result[stepToAdd][plus] = currentPage;
                    Array.Copy(following, 0, result[stepToAdd], 1 + plus, following.Length);

                    if (oldStep != stepToAdd)
                        stepToAdd = oldStep; // continue if the reward was moved
                    pages[i] = null;
                }
                stepToAdd++;
            }

            Debug.Print("======STEPS: {0}========", --stepToAdd);

            return result;
        }

        HtmlPage[] ExtractFollowingPages(HtmlPage page, int pageIndex, HtmlPage[] allPages) {
            if (pageIndex < 0)
                throw new ArgumentException("pageIndex");

            List<HtmlPage> followingPages = new List<HtmlPage>(0);
            if (page.Selects == null || pageIndex >= allPages.Length - 1)
                return followingPages.ToArray();

            foreach (SelectsAct action in page.Selects) {
                if (String.IsNullOrEmpty(action.href))
                    continue;

                string nextPageName = action.ActionName;
                if (nextPageName == null)
                    continue;
                HtmlPage nextPage = allPages.Where(p => p != null && p.name == nextPageName)
                                            .FirstOrDefault();
                if (nextPage == null) {
                    // doesn't match
                    nextPage = allPages.Where(p => p != null && p.name.StartsWith(nextPageName))
                                       .FirstOrDefault();
                }

                if (nextPage != null) {
                    int nextPageIdx = Array.IndexOf(allPages, nextPage);
                    if (nextPageIdx == pageIndex + 2) {
                        // theres's a page between, check if it has only a "finish_dialog" action
                        HtmlPage skippedPage = allPages[pageIndex + 1];
                        if (skippedPage != null && skippedPage.Selects != null) {
                            string name = skippedPage.Selects[0].ActionName;
                            if (name == "finish_dialog") {
                                // add the skipped page before
                                // TODO: check for being non-referenced by other pages?
                                followingPages.Add(skippedPage);
                                allPages[pageIndex + 1] = null;
                            }
                        }
                    }
                    followingPages.Add(nextPage);
                    // remove, so if other pages back-referencing it, the page won't be picked up again
                    allPages[nextPageIdx] = null;
                    followingPages.AddRange(ExtractFollowingPages(nextPage, nextPageIdx, allPages));
                } else if (nextPageName == "check_user_has_quest_item") {
                    // add the next page which is displayed when the check fails
                    // make sure it has "finish_dialog" button
                    // Add to select3 --> select3_1 etc. instead ??
                    // it also can contain "setpro" button !!!
                    int addedCount = 0;
                    string lastAddedPageName = String.Empty;
                    for (int i = pageIndex + 1; i < allPages.Length; i++) {
                        HtmlPage pageAfter = allPages[i];
                        if (pageAfter == null)
                            continue;
                        bool canClose = pageAfter.Selects == null || 
                                        pageAfter.name == "user_item_ok" ||
                                        pageAfter.name == "user_item_fail";
                        if (!canClose && pageAfter.Selects.Length == 1) {
                            string pgAct = pageAfter.Selects[0].ActionName;
                            canClose = pgAct == "finish_dialog" || pgAct.StartsWith("setpro");
                        }
                        if (canClose || pageAfter.name.StartsWith(page.name) ||
                            addedCount > 0 && pageAfter.name.StartsWith(lastAddedPageName)) {
                            followingPages.Add(pageAfter);
                            allPages[i] = null;
                            addedCount++;
                            lastAddedPageName = pageAfter.name;
                            if (!canClose || pageAfter.name == "user_item_ok" || 
                                pageAfter.name == "user_item_fail") {
                                followingPages.AddRange(ExtractFollowingPages(pageAfter, i + 1, allPages));
                            }
                            if (addedCount == 2) // added Accept and Deny pages                         
                                break;
                        }
                    }
                }
            }
            return followingPages.ToArray();
        }

        void FixQuestStep(ref int step, out bool prePageProcessing) {
            // temp Reload
            _questStepMap.LoadQuestStepMap();
            StepProcessOption option = _questStepMap[_quest.id, step];
            step = option.StepMapTo;
            prePageProcessing = option.UsePrepage;
        }

        void AddTabPage(HtmlPage page, bool create, int rewardNo) {
            this.tabPages.SuspendLayout();

            TabPage tab = null;
            TableLayoutPanel wholeTable = null;
            TableLayoutPanel buttonTable = null;
            TransparentRichTextBox textBox = null;

            if (create) {
                wholeTable = new TableLayoutPanel();
                buttonTable = new TableLayoutPanel();
                textBox = new TransparentRichTextBox();
                tabPages.TabPages.Add(page.HtmlPageId.ToString());
                tab = tabPages.TabPages[tabPages.TabPages.Count - 1];
                tab.BackColor = SystemColors.ControlDarkDark;
                tab.BackgroundImage = Properties.Resources.dialogBackground;
                tab.Controls.Add(wholeTable);
                tab.ForeColor = SystemColors.Info;
                tab.Location = new Point(4, 27);
                tab.Margin = new Padding(0);
                tab.Size = new Size(401, 449);
            } else {
                tab = tabPages.TabPages[0];
                wholeTable = tableLayoutPanel1;
                buttonTable = tableLayoutPanel2;
                textBox = transparentRichTextBox1;
            }

            tab.Text = page.HtmlPageId.ToString();
            tab.Tag = tab.ToolTipText = page.name;

            tab.SuspendLayout();
            wholeTable.SuspendLayout();
            buttonTable.SuspendLayout();
            textBox.SuspendLayout();
            this.SuspendLayout();

            if (create) {
                int scrollWidth = SystemInformation.VerticalScrollBarWidth;
                const int rewardsMaxHeight = 230;
                const int totalHeight = 401;

                wholeTable.BackColor = System.Drawing.Color.Transparent;
                wholeTable.ColumnCount = 1;
                wholeTable.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100F));
                wholeTable.Controls.Add(buttonTable, 0, 1);
                wholeTable.Controls.Add(textBox, 0, 0);
                wholeTable.Location = new Point(31, 23);
                wholeTable.RowCount = 2;
                wholeTable.Size = new Size(339, totalHeight);

                bool isReward = page.name.StartsWith("select_quest_reward");
                RewardList rw = null;

                buttonTable.ColumnCount = 1;
                buttonTable.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 100F));
                buttonTable.Dock = DockStyle.Fill;

                int topHeight = 305;

                if (isReward) {
                    int repeat;
                    rw = new RewardList(_quest.GetReward(rewardNo, out repeat))
                    {
                        BackColor = Color.Transparent,
                        Width = 339 - 2 * scrollWidth,
                    };
                    int height = rw.RecommendedHeight;
                    if (height > rewardsMaxHeight)
                        height = rewardsMaxHeight;
                    rw.Height = height;

                    float topPerc = (float)(totalHeight - height) / totalHeight;
                    topHeight = (int)Math.Ceiling(totalHeight * topPerc);
                    wholeTable.RowStyles.Add(new RowStyle(SizeType.Percent, topPerc * 100));
                    wholeTable.RowStyles.Add(new RowStyle(SizeType.Percent, 100f * (1 - topPerc)));

                    buttonTable.Location = new Point(0, topHeight);
                    buttonTable.Margin = new Padding(scrollWidth, 0, 0, 0);
                    buttonTable.RowCount = 1;
                    buttonTable.RowStyles.Add(new RowStyle(SizeType.Percent, 100F));
                    buttonTable.Size = new Size(339 - 2 * scrollWidth, totalHeight - topHeight);
                    rw.RewardSelected += new RewardList.RewardSelectedEventHandler(OnRewardSelected);
                    buttonTable.Controls.Add(rw, 0, 0);
                } else {
                    wholeTable.RowStyles.Add(new RowStyle(SizeType.Percent, 76.05985F));
                    wholeTable.RowStyles.Add(new RowStyle(SizeType.Percent, 23.94015F));

                    buttonTable.Margin = new Padding(0);
                    buttonTable.Location = new Point(0, 305);
                    buttonTable.RowCount = 4;
                    buttonTable.RowStyles.Add(new RowStyle(SizeType.Percent, 25F));
                    buttonTable.RowStyles.Add(new RowStyle(SizeType.Percent, 25F));
                    buttonTable.RowStyles.Add(new RowStyle(SizeType.Percent, 25F));
                    buttonTable.RowStyles.Add(new RowStyle(SizeType.Percent, 25F));
                    buttonTable.Size = new Size(339, 96);
                }

                textBox.BorderStyle = BorderStyle.None;
                textBox.Dock = DockStyle.Fill;
                textBox.Font = new Font("Microsoft Sans Serif", 9.75F, FontStyle.Regular, GraphicsUnit.Point);
                textBox.Location = new Point(0, 0);
                textBox.Margin = new Padding(0);
                textBox.ReadOnly = true;
                textBox.ScrollBars = RichTextBoxScrollBars.None;
                textBox.Size = new Size(339, topHeight);
            }

            buttonPanels.Add(buttonTable);

            StringBuilder text = new StringBuilder();
            if (page.Content != null) {
                if (page.Content.html != null && page.Content.html.body != null &&
                    page.Content.html.body.p != null) {
                    foreach (Paragraph para in page.Content.html.body.p) {
                        if (!String.IsNullOrEmpty(para.visible))
                            continue;
                        string line = para.Value;
                        if (!String.IsNullOrEmpty(line))
                            text.Append(Utility.GetParsedString(line, true));
                        text.Append('\n');
                    }
                }
                if (page.Selects != null) {
                    int i = 0;
                    foreach (SelectsAct action in page.Selects) {
                        if (String.IsNullOrEmpty(action.Value))
                            continue;
                        Button button = new Button();
                        button.BackColor = Color.DarkKhaki;
                        button.ForeColor = SystemColors.ControlText;
                        button.Text = action.Value.Trim('"', ' ', '“', '”');
                        button.Margin = new Padding(1, 1, 1, 1);
                        button.Dock = DockStyle.Fill;
                        ToolTip toolTip = new ToolTip();

                        string actionName = action.ActionName;
                        string emotionName = action.EmotionName;
                        string fixedPage = String.Empty;

                        // check if that page exists
                        HtmlPage matchedPage = _pages.Where(p => p.name == actionName)
                                                     .FirstOrDefault();
                        if (matchedPage == null) {
                            matchedPage = _pages.Where(p => p.name.StartsWith(actionName) &&
                                                            _pages.IndexOf(p) > _pages.IndexOf(page))
                                                .FirstOrDefault();
                            if (matchedPage == null && actionName == "ask_quest_accept") {
                                matchedPage = _pages.Where(p => p.name.StartsWith("quest_accept") &&
                                                            _pages.IndexOf(p) > _pages.IndexOf(page))
                                                    .FirstOrDefault();
                                fixedPage = String.Format("(wrong!!! Id = {0}, Name = ask_quest_accept)", 
                                                          HtmlPage.Index["ask_quest_accept"]);
                            }
                        }

                        string tip = String.Empty;
                        if (matchedPage != null) {
                            // create click handler
                            button.Click += new EventHandler(delegate(object sender, EventArgs args)
                            {
                                var allPages = tabPages.TabPages.Cast<TabPage>();
                                var showPage = allPages.Where(t => ((string)t.Tag) == matchedPage.name &&
                                                                   !t.Equals(tab))
                                                       .FirstOrDefault();
                                if (showPage != null) {
                                    tabPages.SelectedTab = showPage;
                                }
                            });
                            tip = String.Format("Action Id = {0}, Page Id = {1}",
                                                action.Id, matchedPage.HtmlPageId);
                        } else {
                            tip = String.Format("Action Id = {0}, Name = {1}", action.Id, actionName);
                        }

                        if (!string.IsNullOrEmpty(fixedPage))
                            tip += fixedPage;

                        if (!String.IsNullOrEmpty(emotionName))
                            tip += String.Format(", Emotion = {0}", emotionName);

                        toolTip.SetToolTip(button, tip);

                        if (i > 3)
                            continue;
                        buttonTable.Controls.Add(button, 0, i++);
                    }
                }
            }

            textBox.Text = text.ToString();
            textBoxes.Add(textBox);

            tabPages.ResumeLayout(true);
            tab.ResumeLayout(true);
            wholeTable.ResumeLayout(true);
            buttonTable.ResumeLayout(true);
            textBox.ResumeLayout(true);
            this.ResumeLayout(false);
        }

        ItemDetails _detailsForm = null;

        void OnItemLinkClicked(object sender, LinkLabelLinkClickedEventArgs args) {
            if (args.Button != MouseButtons.Left)
                return;
            if (args.Link.LinkData == null)
                return;
            Item item = (Item)args.Link.LinkData;
            CreateAndShowDetails(item);
            LinkLabel label = (LinkLabel)sender;
            label.Focus();
        }

        void OnRewardSelected(object sender, Item args) {
            CreateAndShowDetails(args);
            var control = (RewardList)sender;
            control.EnsureItemFocused();
        }

        void CreateAndShowDetails(Item item) {
            if (_detailsForm != null && !_detailsForm.IsDisposed) {
                _detailsForm.Close();
                _detailsForm.Dispose();
            }
            Form topLevel = (Form)this.TopLevelControl;
            _detailsForm = new ItemDetails(topLevel, item);
            _detailsForm.Owner = topLevel;
            _detailsForm.FormClosed += new FormClosedEventHandler(OnFormClose);
            _detailsForm.Disposed += new EventHandler(OnFormClose);
            topLevel.SizeChanged += new EventHandler(OnLocationChanged);
            topLevel.LocationChanged += new EventHandler(OnLocationChanged);
            topLevel.FormClosing += new FormClosingEventHandler(OnFormClose);
            _detailsForm.Show();
        }

        void OnFormClose(object sender, EventArgs args) {
            if (_detailsForm == null || _detailsForm.IsDisposed)
                return;

            Form topLevel = (Form)this.TopLevelControl;
            _detailsForm.FormClosed -= new FormClosedEventHandler(OnFormClose);
            _detailsForm.Disposed -= new EventHandler(OnFormClose);
            if (!sender.Equals(this)) {
                topLevel.SizeChanged -= new EventHandler(OnLocationChanged);
                topLevel.LocationChanged -= new EventHandler(OnLocationChanged);
                topLevel.FormClosing -= new FormClosingEventHandler(OnFormClose);
            }

            if (sender.Equals(this) || sender.Equals(this.TopLevelControl)) {
                var form = _detailsForm;
                _detailsForm = null;
                form.Close();
            }
        }

        void OnLocationChanged(object sender, EventArgs args) {
            if (_detailsForm != null && _detailsForm.IsHandleCreated)
                _detailsForm.AdjustToParent();
        }
    }
}
