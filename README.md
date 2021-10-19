# On The Santaló Regions of Centrally Symmetric Convex Bodies
## Introduction
This repository contains the Java program for the project "On the Santaló Regions of Centrally Symmetric Convex Bodies" by Yanir Rubinstein and Chad Yu.

The program can display the Santaló Region of an inputted 2-dimensional convex body, created by Chad Yu of Montgomery Blair High School. The Santaló Regions, a notion of convex geometry first introduced by Elisabeth Werner and Mathieu Meyer, are defined to be the points "x" on the interior of a convex body such that the product of the volume of the body and the volume of the polar of the body translated by "x" are less than a certain value. The scalar that affects this certain value is called the distortion parameter. 

This has direct implications in convex geometry, as it serves as a visualization of two dimensional Santaló Regions for future researchers. At glance, the definition for the Santaló seems relatively abstract, and this helps to develop more intuition on the topic by providing a visual representation of these bodies. In general, the study of Santaló Regions could be further be useful in convex geometry, as it could possibly serve as a unique characterization of a given convex body. Furthermore, certain bounds could be placed on the distortion parameter for the Santaló Region of a body to determine how it relates to the lower bound of Mahler's Conjecture, an unsolved problem for almost a century. 

For our project, my program helped to demonstrate certain properties of the Santaló Regions and indicate possibilities at the relation of the Santaló Regions to ellipses. Specifically, our two main iterative procedures show a convergence of a sequence of Santaló Regions to an ellipse through a "smoothing" of the Santaló Regions through the iterations. This further hints at a connection between the Santaló Regions and Mahler's Conjecture, as the ellipsoid is the unique maximizer of the Mahler Volume (product of volume of a body and volume of its polar) in all dimensions. 
## Instructions for User
### Setting Up the IDE and Project from Repository
One way to run this program is through the Eclipse Integrated Development Environment (IDE). Once you download the latest version of Eclipse, the folder in this repository, and your workspace is set up, select File -> Open Projects from File System -> the folder where you saved the program -> Sántalo Regions folder. This allows the user to both create their own version of the program or run iterative procedures of the Sántalo Regions.
### Running the Program
Once the user runs the program, they will be prompted with the following:
```
Enter in the type of body: 
```
Although the program is mostly used for users to input points on the boundary of the body, I also developed a few common convex bodies whose boundaries are computed by the program for the Santaló Regions algorithm. I did this for a circle with a set radius, a convex body defined by a homogeneous fourth degree polynomial in two variables, a fixed ellipse, and a regular polygon whose number of sides and size is determined by user input. The keywords for each of these bodies is "circle", "four", "ellipse", and "regular", respectively. If the user enters "regular", they are then prompted for the following:
```
Enter in the number of sides: 
```
After entering an integer value for the number of edges of the regular polygon, the user is prompted for the radius of the circumscribed circle of the regular polygon (distance from each vertex to the center of mass):
```
Enter in the circumscribed radius: 
```
For a body whose points are manually inputted into the program, the user enters in "input" for the type of body and is prompted for the following:
```
Enter point x,y seperated by a comma or type "done": 
```
The user is prompted this until they enter "done", when all of the points are finished being inputted. Regardless of the type of body, once the basic inputs that are necessary to construct the body are in place, then the user is prompted for which procedure to run as follows:
```
Iterative or Distortion Procedure? 
```
If the user chooses the iterative procedure, they are followed up for other inputs:
```
Normalized version? 
Enter the number of iterations: 
Enter the distortion factor (t):
Enter translation vector: 
```
In the iterative procedure, the Santaló Region is found for the body that is inputted in by the user, and passed to the next iteration as the convex body to be processed. Then the Santaló Region is found of this body, and the process is repeated. The first procedure-specific input that is asked is for the normalization of the volume of the body for each procedure. In this case, the resulting Santaló Region is first normalized to a fixed volume before proceeding to the next iteration, so the visualization of convergence is more apparent. The second input takes in the number of iterations, and the last input takes in the distortion factor, which affects the size and shape of the Santaló Region through the volume product condition. The translation vector determines by how much the body is translated. The translation is usually 0, but the author included this for experimentation on iterations  An example of the iterative procedure with normalization is shown below.
<img width="1112" alt="Screen Shot 2021-10-19 at 10 19 54 AM" src="https://user-images.githubusercontent.com/73807846/137929432-87f176bb-5faf-4335-a14b-c42c1849300d.png">
In the distortion procedure, the Santaló Region is found for the inputted convex body. Then, the minimum distortion is found, which is the smallest distortion such that the Santaló Region is nonempty. The sequence of convex bodies with distortions ranging from the minimum distortion to a specified distortion are displayed. For this procedure, the following inputs are prompted:
```
Normalized Version?
Enter constant area: 
Enter translation vector: 
```
The prompt for normalization and translation obtains the same values as for the iterative procedure. But in this procedure specifically, the prompt for a constant area gets the fixed value to which the body's area is normalized to for each iteration. An example of the distortion procedure is shown below:
<img width="1112" alt="Screen Shot 2021-10-19 at 1 07 45 PM" src="https://user-images.githubusercontent.com/73807846/137958465-140c004a-f949-4a4a-a003-2d5f62cad36b.png">
The frame for the program is the section of the xy plane with x and y coordinates ranging from -5 to 5. The user can feel free to change this after downloading the repository. 
## Attribution
If you use this code in your reseach, please cite my name (Chad Yu) and either my previous educational institution (Montgomery Blair High School), which I was attending while developing this program, or my current place of study (Cornell University). The paper for our research is still in progress.
