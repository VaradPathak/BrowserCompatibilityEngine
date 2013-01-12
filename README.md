BrowserCompatibilityEngine
==========================

This simple java program converts your existing IE 6 compatible JSP and Javascript code to IE 9,  Mozzila 18 compatible code.
This is achieved by making the existing code W3C compliant by using simple functions provided by JQuery.

Features provided by Browser Compatibility Engine

-          Search for all JSP files from the parent folder to all the subfolders

-          Write a separate method for each type of change that we are making. 
            Keep on modifying each one of these methods to improve the performance

-          Replace all old functions with new ones which are in line with W3C recommendations

-          The logical errors and changes that are required will be done on top of the changes made by the Engine. 
            This has to be done by Humans.

-          Some issues missed can be resolved while testing the application.

-          Write new JSP/JS files in new W3C folder which is below the parent folder and 
             would have same sub-folder directory structure as the parent folder.

 
Functions  implemented till now:
-          Replace &nbsp;

-          Convert HTML comments to JSP comments

-          Replace .innerText with .innerHTML

-          Replace document.all[] with $get() functions

-          Read all the files in directory structure

-          Write the newly generated files in W3C folder having same sub-folder directory structure

-          Correct/replace <script> tag with <script type="text/javascript">

-          Add the following init part to every JSP except the init_body.jsp. 
            This Init part is modifiable as it is read from another jsp called init.jsp stored at different location.

-          Handled multiline comments.
