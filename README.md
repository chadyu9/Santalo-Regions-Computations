# On The Santaló Regions of Centrally Symmetric Convex Bodies
## Introduction
This repository contains the Java program for the project "On the Santaló Regions of Centrally Symmetric Convex Bodies" by Yanir Rubinstein and Chad Yu.

The program can display the Santaló Region of an inputted 2-dimensional convex body, created by Chad Yu of Montgomery Blair High School. The Santaló Regions, a notion of convex geometry first introduced by Elisabeth Werner and Mathieu Meyer, are defined to be the points "x" on the interior of a convex body such that the product of the volume of the body and the volume of the polar of the body translated by "x" are less than a certain value. The scalar that affects this certain value is called the distortion parameter. 

This has direct implications in convex geometry, as it serves as a visualization of two dimensional Santaló Regions for future researchers. At glance, the definition for the Santaló seems relatively abstract, and this helps to develop more intuition on the topic by providing a visual representation of these bodies. In general, the study of Santaló Regions could be further be useful in convex geometry, as it could possibly serve as a unique characterization of a given convex body. Furthermore, certain bounds could be placed on the distortion parameter for the Santaló Region of a body to determine how it relates to the lower bound of Mahler's Conjecture, an unsolved problem for almost a century. 

For our project, my program helped to demonstrate certain properties of the Santaló Regions and indicate possibilities at the relation of the Santaló Regions to ellipses. Specifically, our two main iterative procedures show a convergence of a sequence of Santaló Regions to an ellipse through a "smoothing" of the Santaló Regions through the iterations. This further hints at a connection between the Santaló Regions and Mahler's Conjecture, as the ellipsoid is the unique maximizer of the Mahler Volume (product of volume of a body and volume of its polar) in all dimensions. 
## Instructions for User
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
For a body whose points are manually inputted into the 
