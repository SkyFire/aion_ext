using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace AionQuests
{
    static class TreeNodeExtensions
    {
        public static IEnumerable<TreeNode> GetNodes(this TreeNode rootTreeNode, int level) {
            if (rootTreeNode == null || rootTreeNode.Level > level)
                yield break;
            if (rootTreeNode.Level == level)
                yield return rootTreeNode;
            foreach (TreeNode node in rootTreeNode.Nodes) {
                IEnumerable<TreeNode> subNodes = GetNodes(node, level);
                foreach (TreeNode subNode in subNodes)
                    yield return subNode;
            }
        }
    }
}
