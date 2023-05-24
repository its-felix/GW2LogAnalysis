Project Description:
Collect data from all JSON files in a folder, grab the data you have selected from the settings tab, sort and output the information into a csv file for excel. 
Resources used:
•	Eclipse IDE (for Java) 
•	Jackson Library (for xml): 
o	Jar files: https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml/2.9.7
o	Javadocs: https://www.javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/2.9.7/index.html 
•	Simple JSON:
•	Swing (for GUI)
 
Action Items:
•	Phase one: Initialization
	o	Customize GUI to allow for all the following via main menu settings. 
	o	Settings, simple exclusion filters (Name, duration, and location. Any information that can easily be accessed before gathering all the tables.) 
	o	Xml settings import and export into/from a simple class. 
	o	JSON Read files in selected forlder, using simple exclusion filter. 
	o	Export csv file with the JSON information. Just put relevent settings up top and then all of the found JSON info in columns below. 
•	Phase Two: Core
	o	GUI to allow for following features. Going into the settings tab for some of them.  
	o	Create display filters. Could use lists to show what output sections (object) should be displayed. (e.g. “Healing” would exclude damage.)
		Get the base in for boons, but only allow “no boons” or “all boons”? This should be a list of checkable boons, with “(un)select all” and a selection option for different boon settings (active phase, all, etc.) 
		Get the base for different features, such as consumable in to a selectable table.  Also a list of checkable options. No need to get far or find all values. 
	o	Export csv with spaces between “Areas” (general, damage, heals, boons, conditions, etc.) 
•	Phase Three: Features
	o	GUI to allow for the following features. 
	o	Further all filters: 
		Boon table needs to be able to select specific boons. 
		Finish the selection for different features, such as consumables. 
		Add in selection for class specific stats. Everything avaiable in Arc DPS’s boon table should be available for selection. 
	o	Add further exclusion filtering (weapon selecitons, Profession, percent alive. Etc.) 
	o	Selecting a Directory for JSON reading should count the number of files available for reading?
	o	Add a loading bar while reading files. 
	o	Persist settings on close: Save and load previously used settings when closing and loading the application. 
•	Phase Four: Expand
	o	Expand options for Strikes, raids, and other environments. I do not currently know all of the changes that would be made, but there are many examples to read from. 
	o	Expand the output options as well. 
•	Phase Five: Finalize
	o	Time and optimize running. 
	o	Simplify for building. 
	o	Create readme highlighting utility and use. 
•	Phase Six: Build and deploy. 


OUTPUT:
The output will be formatted for excel csv. Basically, a comma between each column. To move down a row use a new line, or “\n”. More details found at https://www.spreadsheetsmadeeasy.com/understanding-csv-files-in-excel/ 
Basic details and room for comments will be left at the top of the spreadsheet, then the format for a table will be inserted, the the headers stating the information below it. It may end up being a wide table, or long if many options or logs are parsed. To help this, we could use a spacing between different sections on the x axis (still have the same information in the row), between general, dps, healing, and boons. This will make the format more readable. 
Settings chosen by the user may be saved or loaded via xml file. By using the Jackson library, we are able to easily read from or fill a class object that stores the settings. 


