This application parses Aion client HTML, XML files and displays information about quests.
Create data folder where the binary is placed with the following structure:

\data\dialogs
\data\items
\data\npcs
\data\quest
\data\strings
\data\dialogs\10000_19999
\data\dialogs\20000_29999
\data\dialogs\30000_39999
\data\dialogs\40000_49999
\data\items\pics

Put the client files in the folders

\data\dialogs\HtmlPages.xml
\data\dialogs\HyperLinks.xml
\data\items\client_combine_recipe.xml
\data\items\client_items.xml
\data\npcs\client_npcs.xml
\data\quest\quest.xml
\data\strings\client_strings.xml

All dialog HTML files have to be placed into \data\dialogs\ and its subfolders, similar to client structure.
You will need to fix some of these files (removing duplicate <steps> tags, changing UTF-16 to UTF-8 in some files,
and removing invalid HTML comments(<!---), some files may have missing closing </body> tags or they are duplicate.
If the file is invalid, then you will see only a node in the application, without a content.

Put all item images into \data\items\pics folder in .png format. Do .dds to .png batch conversion
using IrfanView application for example with the cropping to 40x40 option.

During the first run, application will load all HTML dialog files and create quests.dat file for faster loading
the next time. It will take half an hour or so. Once completed, you can delete all HTML files from dialogs 
folder then.



