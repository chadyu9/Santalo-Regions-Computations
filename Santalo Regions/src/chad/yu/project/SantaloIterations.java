package chad.yu.project;

/**
 * These are the import statements. They import classes created by Java so that functions
 * in these classes which perform certain tasks can be used. 
 */
import java.awt.BorderLayout;//used to format canvas to draw on (line 103)
import java.awt.Color;
import java.awt.Graphics2D;//used to draw the actual shapes
import java.awt.Shape;//(lines 39,41,42,68,86,87,224,233,242)
import java.awt.geom.Line2D;

import java.awt.event.MouseAdapter;//used to detect when mouse actions are performed
import java.awt.event.MouseEvent;//(lines 37-38,54-55,66-67)
import java.awt.event.MouseMotionAdapter;

import java.util.ArrayList;//allows usage of ArrayList, which helps create collections of points
//(used in all instance variables/methods of the program)
import java.util.Scanner;

import javax.swing.JFrame;//JFrame and JPanel are used to create the actual canvases on which
import javax.swing.JPanel;//objects are drawn (lines 27, 102-106)

import java.math.BigDecimal;//Helps with precision of decimals and rounding 
import java.math.RoundingMode;//(used in most instances where a decimal is calculated)

//class declaration: the class contains all methods necessary to carry out actions
public class SantaloIterations extends JPanel{
    private static ArrayList<double[]> points = new ArrayList<double[]>();//collection/array of points which composes of the boundary of the original body
    static ArrayList<double[]> polarPoints = new ArrayList<double[]>();//array of points making the polar of the body
    static ArrayList<double[]> SantaloPoints = new ArrayList<double[]>();//array of points on boundary of 'good' points
    static ArrayList<double[]> secondPoints = new ArrayList<double[]>();
    private static ArrayList<double[]> inside = new ArrayList<double[]>();//array of points which contains all points on the interior of the body
    private static int numItr;
    private static double distort;
    private static double initialD; 
    private static String iterationMode;
    private static String norm;
    private static double areaNorm;
    
	/**
     * This is the constructor of the class which outlines what to draw on the canvas (all three curves)
     */
    public SantaloIterations() {
    	addMouseListener(new MouseAdapter() {//detects when the mouse is released
    		public void mouseClicked(MouseEvent e) {
    			if (iterationMode.toLowerCase().equals("distortion")) {
    	    		
	    			Graphics2D g2 = (Graphics2D)getGraphics();//sets up object which is used to draw shapes
	    			//draws coordinate axes
					g2.draw((Shape) new Line2D.Double(500, 0, 500, 1000));
					g2.draw((Shape) new Line2D.Double(0, 500, 1000, 500));
					
					ArrayList<double[]> firstGoodPoints = new ArrayList<double[]>();
	    			for (int i = 0; i < numItr; i++) {
	    				findPolar();//finds the polar of the body
	    				
	    				findInterior();//finds points on interior of the body
	    				findSantalo();//finds the good points
	
	    				//draws the bodies
	    				if (i == 0) {
	    					g2.setColor(Color.BLUE);
	    					drawBody(points, g2);
	    					for (int j = 0; j < SantaloPoints.size(); j++) {
	    						firstGoodPoints.add(SantaloPoints.get(j));
	    					}
	    				}
	    				
	    				if (norm.toLowerCase().equals("yes")) {
	    					SantaloPoints = normalize(SantaloPoints, areaNorm);
	    				}
	    				System.out.println(SantaloPoints.size());
	    				g2.setColor(Color.GREEN);
	    				drawBody(SantaloPoints, g2);
	    				
	    				polarPoints.clear();
	    				inside.clear();
	    				
	    				if (i != numItr-1) {
	    					distort += 0.01;
	    				}	
	    				SantaloPoints.clear();
	    				System.out.println(i);
	    				try {
							Thread.sleep(130);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    			}
	    			for (int v = 0; v < firstGoodPoints.size(); v++) {
	    				System.out.println(firstGoodPoints.get(v)[0]+ " "+firstGoodPoints.get(v)[1]);
	    			}
	    			System.out.println("Minimum Distortion Factor: "+ initialD);
	    			System.out.println("Complete.");
    			}
    			else if (iterationMode.toLowerCase().equals("iterative")) {
    				System.out.println(distort + " " + initialD);
    				System.out.println(Math.log(distort-initialD)/Math.log(10));
    				Graphics2D g2 = (Graphics2D)getGraphics();//sets up object which is used to draw shapes
        			//draws coordinate axes
    				g2.draw((Shape) new Line2D.Double(500, 0, 500, 1000));
    				g2.draw((Shape) new Line2D.Double(0, 500, 1000, 500));
    				
    				double bodyArea = shoelace(points);
    				if (norm.toLowerCase().equals("yes")) {
	    				for (int k = 0; k < points.size(); k++) {
	        				double newX = points.get(k)[0]/Math.pow(bodyArea,0.5);
	        				double newY = points.get(k)[1]/Math.pow(bodyArea,0.5);
	        					
	        				points.set(k, new double[] {newX,newY});
	       				}
    				}
        			for (int i = 0; i < numItr; i++) {
        				findPolar();//finds the polar of the body
        				
        				findInterior();//finds points on interior of the body
        				findSantalo();//finds the good points
        		
        				//draws the bodies
        				g2.setColor(Color.BLUE);
        				drawBody(points, g2);
        				g2.setColor(Color.RED);
        				drawBody(polarPoints,g2);
        				g2.setColor(Color.GREEN);
        				drawBody(SantaloPoints, g2);
        				
        				points.clear();
        				polarPoints.clear();
        				inside.clear();
        				
        				if (i != numItr-1) {
        					for (int j = 0; j < SantaloPoints.size(); j++) {
        						double x = SantaloPoints.get(j)[0];
        						double y = SantaloPoints.get(j)[1];
        						
        						points.add(new double[] {x,y});
        					}
        					points = removeRepeat2(points);//accounts for errors created when too many horizontal lines between consecutive points
        					points = fixValues(points);//accounts for errors created when the body becomes self-intersecting
        					points = removeRepeat(points);//accounts for not-a-function error
        					points = makeHullPresorted(points);//further fixes any concave errors with convex hull algorithm
        					bodyArea = shoelace(points);
        					if (norm.toLowerCase().equals("yes")) {
	        					for (int k = 0; k < points.size(); k++) {
	            					double newX = points.get(k)[0]/Math.pow(bodyArea,0.5);
	            					double newY = points.get(k)[1]/Math.pow(bodyArea,0.5);
	            					
	            					points.set(k, new double[] {newX,newY});
	           					}
        					}
        				}	
        				if (i == numItr-2) {
        					for (int index = 0; index < SantaloPoints.size(); index++) {
            					double transferX = SantaloPoints.get(index)[0];
            					double transferY = SantaloPoints.get(index)[1];
            					
            					secondPoints.add(new double[]{transferX,transferY});
            				}
        				}
        				for (int index = 0; index < SantaloPoints.size(); index++) {
        					System.out.println(SantaloPoints.get(index)[0] + " " + SantaloPoints.get(index)[1]);
        				}
        				SantaloPoints.clear();
        				System.out.println(i);
        			}
        			for (int index = 0; index < secondPoints.size(); index++) {
    					System.out.println(secondPoints.get(index)[0] + " " + secondPoints.get(index)[1]);
    				}
        			System.out.println("Complete.");
    			}
    		}
    	});
    }
    
    /**
     * This is the main method which actually runs the functions necessary.
     * @param args
     */  
    public static void main(String[]args) {
    	Scanner reader = new Scanner(System.in);
    	System.out.println("Enter in the type of body: ");
    	String bodyType = reader.nextLine();
    	if (bodyType.toLowerCase().equals("input")) {
    		String inStr;
        	while (true){
        		System.out.print("Enter point x,y separated by comma or type \"done\": ");
        		inStr = reader.nextLine();
        		if (inStr.equals("done")) {
        			break;
        		}
        		String[] coordStr = inStr.split(",");
        		double currX = Double.parseDouble(coordStr[0]);
        		double currY = Double.parseDouble(coordStr[1]);
        		points.add(new double[] {currX,currY});
        	}
    	}
    	else if (bodyType.toLowerCase().equals("regular")) {
    		System.out.println("Enter in the number of sides: ");
    		int numSides = reader.nextInt();
    		System.out.println("Enter the radius of the circumscribed circle: ");
    		double circRad = reader.nextDouble();
    		
    		addPointsRegular(numSides, circRad);
    	}
    	else if (bodyType.toLowerCase().equals("circle")) {
    		addPointsCircle();
    	}
    	else if (bodyType.toLowerCase().equals("ellipse")) {
    		addPointsEllipse();
    	}
    	else if (bodyType.toLowerCase().equals("four")) {
    		addPointsFour();
    	}
    	Scanner reader2 = new Scanner(System.in);
    	System.out.println("Iterative or Distortion Procedure?");
    	iterationMode = reader2.nextLine();
    	
    	if (iterationMode.toLowerCase().equals("iterative")) {
    		System.out.println();
        	System.out.print("Normalized version? ");
        	norm = reader.nextLine();
        	
    		System.out.println();
        	System.out.print("Enter the number of iterations: ");
        	numItr = reader.nextInt();
        	
        	System.out.println();
        	System.out.print("Enter the distortion factor (t): ");
        	distort = reader.nextDouble();
        	
        	findPolar();
        	initialD = shoelace(points)*shoelace(polarPoints)/(Math.pow(Math.PI, 2));
        	polarPoints.clear();
    	}
    	else if (iterationMode.toLowerCase().equals("distortion")) {
    		Scanner reader3 = new Scanner(System.in);
    		System.out.println("Normalized version? ");
    		norm = reader3.nextLine();
    		
    		if (norm.toLowerCase().equals("yes")) {
    			System.out.println("Enter constant area: ");
    			areaNorm = reader3.nextDouble();
    		}
    		
        	distort = 0.0;
        	findPolar();
        	distort = shoelace(points)*shoelace(polarPoints)/(Math.pow(Math.PI, 2));
        	initialD = distort;
        	
        	distort = 0.01*(Math.ceil(distort*100));
        	
        	polarPoints.clear();
        	numItr = (int)((1.5 - distort)*100) + 1; 
    	}
    	
    	System.out.println("Enter translation vector: ");
    	String transVec = reader2.nextLine();
		String[] coordStr = transVec.split(",");
		double transX = Double.parseDouble(coordStr[0]);
		double transY = Double.parseDouble(coordStr[1]);
		for (int i = 0; i < points.size(); i++) {
			double newX = points.get(i)[0];
			double newY = points.get(i)[1];
			
			newX -= transX;
			newY -= transY;
			points.set(i, new double[] {newX,newY});
		}
	
    	JFrame frame = new JFrame("General Body([-5,5]x[-5,5])");//constructs the canvas on which curves are drawn
        frame.getContentPane().add(new SantaloIterations(), BorderLayout.CENTER);//declares that the content of GeneralBody1() from above are displayed on the canvas
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);//sets dimensions
        frame.setVisible(true);
    }
    
    /**
     * This adds the points of an example circle as the main points body.
     */
    public static void addPointsCircle() {
    	//creates first half points on the boundary based on def of function x^2+y^2<=4
    	for (int i = -200; i < 200; i++) {
    		double xCoord = i/100.0;
    		double yCoord = Math.pow((4-Math.pow(xCoord, 2)), 0.5);//def of function
    		yCoord = BigDecimal.valueOf(yCoord).setScale(13, RoundingMode.HALF_UP).doubleValue();//makes precise decimal
    		points.add(new double[] {xCoord,yCoord});
    	}	
    	int size = points.size();
    	//creates the second half of the points by symmetry
    	for (int i = 0; i < size; i++) {
    		double newX = points.get(i)[0];
    		double newY = points.get(i)[1];
    		newX *= -1;
    		newY *= -1;
    		points.add(new double[] {newX,newY});
    	}
    }
    
    /** 
     * This adds the points of an example ellipse to the main points body.
     */
    public static void addPointsEllipse() {
    	//creates first half points on the boundary based on def of function x^2+y^2<=4
    	for (int i = -300; i < 300; i++) {
    		double xCoord = i/100.0;
    		double yCoord = Math.pow((36-4*Math.pow(xCoord, 2)), 0.5)/3;//def of function
    		yCoord = BigDecimal.valueOf(yCoord).setScale(13, RoundingMode.HALF_UP).doubleValue();//makes precise decimal
    		points.add(new double[] {xCoord,yCoord});
    	}	
    	int size = points.size();
    	//creates the second half of the points by symmetry
    	for (int i = 0; i < size; i++) {
    		double newX = points.get(i)[0];
    		double newY = points.get(i)[1];
    		newX *= -1;
    		newY *= -1;
    		points.add(new double[] {newX,newY});
    	}
    }
    
    /**
     * This adds the points of a sample body with the equation x^4+y^4 <= 81.
     */
    public static void addPointsFour() {
    	//creates first half points on the boundary based on def of function x^4+y^4<=81
    	for (int i = -300; i < 300; i++) {
    		double xCoord = i/100.0;
    		double yCoord = Math.pow((81-Math.pow(xCoord, 4)), 0.25);//def of function
    		yCoord = BigDecimal.valueOf(yCoord).setScale(13, RoundingMode.HALF_UP).doubleValue();//makes precise decimal
    		points.add(new double[] {xCoord,yCoord});
    	}	
    	int size = points.size();
    	//creates the second half of the points by symmetry
    	for (int i = 0; i < size; i++) {
    		double newX = points.get(i)[0];
    		double newY = points.get(i)[1];
    		newX *= -1;
    		newY *= -1;
    		points.add(new double[] {newX,newY});
    	}
    }
    
    /**
     * This adds the boundary of a regular polygon to the main points.
     * @param sides number of edges of the polygon
     * @param radius circumradius of the polygon
     */
    public static void addPointsRegular(double sides, double radius) {
    	double degSector = 360/sides;
    	double radSector = Math.toRadians(degSector);
    	
    	for (int i = 0; i < (int)sides; i++) {
    		double xCoord = radius*Math.cos(i*radSector);
    		double yCoord = radius*Math.sin(i*radSector);
    		
    		points.add(new double[] {xCoord,yCoord});
    	}
    }
    
    /**
     * This method volume normalizes a convex body to have a certain constant area. 
     * @param arrPoints array of points which is the boundary of the body
     * @param area constant area to normalize it to
     * @return array of normalized body to that constant area
     */
    public static ArrayList<double[]> normalize(ArrayList<double[]>arrPoints, double area){
    	double bodyArea = shoelace(arrPoints);
		for (int k = 0; k < arrPoints.size(); k++) {
			double newX = area*arrPoints.get(k)[0]/Math.pow(bodyArea,0.5);
			double newY = area*arrPoints.get(k)[1]/Math.pow(bodyArea,0.5);
			
			arrPoints.set(k, new double[] {newX,newY});
		}
		return arrPoints;
    }
    /**
     * This is the method that finds the area of a set of points by shoelace formula.
     * @param arr the array of points
     * @return the area of the polygon created by connecting the points
     */
    public static double shoelace(ArrayList<double[]> arr) {
    	if (arr.size() < 3)
    		return 0;
    	double shoeArea = 0.0;
    	//adds x1*y2 - y2*x1 for every pair of consecutive points (x1,y1) and (x2,y2)(shoelace)
    	for (int i = 0; i < arr.size()-1; i++) {
    		double x1 = arr.get(i)[0];
    		double y1 = arr.get(i)[1];
    		double x2 = arr.get(i+1)[0];
    		double y2 = arr.get(i+1)[1];
    		
    		shoeArea += x1*y2 - x2*y1;
    	}
    	
    	//completes the sum with the last and first points as consecutive points
    	double lx = arr.get(arr.size()-1)[0];
    	double ly = arr.get(arr.size()-1)[1];
    	double fx = arr.get(0)[0];
    	double fy = arr.get(0)[1];
    	shoeArea += lx*fy - fx*ly;
    	
    	return 0.5*Math.abs(shoeArea);//area by shoelace formula
    }
    
    /**
     * This method finds the polar of the original body.
     */
    public static void findPolar() {
    	for (int i = 0; i < points.size(); i++) {
    		double x1,y1,x2,y2,x,y;//variables: current and adjacent coordinates
    		if (i == 0) {//when i = 0, there would be index out of bound errors
    			x1 = points.get(points.size()-1)[0];
    			y1 = points.get(points.size()-1)[1];
    			x2 = points.get(1)[0];
    			y2 = points.get(1)[1];
    		}
    		else if (i == points.size()-1) {//when i is the last point, special case
    			x1 = points.get(points.size()-2)[0];
    			y1 = points.get(points.size()-2)[1];
    			x2 = points.get(0)[0];
    			y2 = points.get(0)[1];
    		}
    		else {//(x1,y1), (x2,y2) are just adjacent points to the current point
    			x1 = points.get(i-1)[0];
    			y1 = points.get(i-1)[1];
    			x2 = points.get(i+1)[0];
    			y2 = points.get(i+1)[1];
    		}
    		//current point
    		x = points.get(i)[0];
    		y = points.get(i)[1];
    		
    		//secant slope and segment slope represent two different hyperplanes
    		double slope1 = (y-y1)/(x-x1);
    		double slope = (y2-y1)/(x2-x1);
    		
    		//finds the angle between hyperplane of segment and normal vector to hyperplane
    		double angle1;
    		double supportInit = 0;
    		//vertical or horizontal hyperplane
    		if (y==y1) {
    			angle1 = 0.5*Math.PI;
    			polarPoints.add(new double[] {0,1/y});
    		}
    		else if (x==x1) {
    			angle1 = 0;
    			polarPoints.add(new double[] {1/x,0});
    		}
    		//other case
    		else {
    			double perpslope = -1/slope1;//slope of normal vector
    			angle1 = Math.atan(perpslope);
    			double hyperX = (slope1*x-y)/(slope1+(1/slope1));//intersection of normal vector and hyperplane
    			if (hyperX < 0) {//when angle > pi/2
    				angle1 += Math.PI;
    			}
    			
    			//support: distance from origin to hyperplane
    			supportInit = Math.abs(slope1*x-y)/(Math.pow((Math.pow(slope1,2)+1), 0.5));
        		polarPoints.add(new double[] {Math.cos(angle1)/supportInit, Math.sin(angle1)/supportInit});//scaled down for def of polar
    		}
    		
    		//same procedure as above but with the secant slope
    		double angle;
    		double support = 0.0;
    		if (y2 == y1) {
    			angle = 0.5*Math.PI;
    			polarPoints.add(new double[] {0, 1/y});
    		}
    		else if (x2 == x1) {
    			angle = 0.0;
    			polarPoints.add(new double[] {1/x, 0});
    		}
    		else {
    			double perpSlope = -1/slope;
    			angle = Math.atan(perpSlope);
    			double hyperX = (slope*x-y)/(slope+(1/slope));
    			if (hyperX < 0) {
    				angle += Math.PI;
    			}
    			
    			support = Math.abs(slope*x-y)/(Math.pow((Math.pow(slope, 2)+1), 0.5));
    			polarPoints.add(new double[] {Math.cos(angle)/support, Math.sin(angle)/support});
    		}
    	}
    }
    
    /**
     * This method takes a set of points and draws it the body with those points as the boundary
     * @param pointArr set of points
     * @param g Graphics2D object used to draw body
     */
    public void drawBody(ArrayList<double[]>pointArr, Graphics2D g) {
		for (int i = 0; i < pointArr.size()-1; i++) {
			double currentX = 100*pointArr.get(i)[0];//current x and y
			double currentY = 100*pointArr.get(i)[1];
			
			double nextX = 100*pointArr.get(i+1)[0];//next x and y
			double nextY = 100*pointArr.get(i+1)[1];
			
			//draws line connecting the two points
			g.draw((Shape)new Line2D.Double(unX(currentX,1000),unY(currentY,1000),unX(nextX,1000),unY(nextY,1000)));
		}
		
		//same process connecting the last point and first point
		double firstX = 100*pointArr.get(0)[0];
		double firstY = 100*pointArr.get(0)[1];
		
		double lastX = 100*pointArr.get(pointArr.size()-1)[0];
		double lastY = 100*pointArr.get(pointArr.size()-1)[1];
		g.draw((Shape)new Line2D.Double(unX(lastX,1000),unY(lastY,1000),unX(firstX,1000),unY(firstY,1000)));
    }
    
    /**
     * This method is used to find the polar of a translation of the original body.
     * @param translateX how much the x coordinates of the original body are translated
     * @param translateY how much the y coordinates of the original body are translated
     */
    public static ArrayList<double[]> getPolarInterior(double translateX, double translateY) {
    	ArrayList<double[]> polarInterior = new ArrayList<double[]>();
    	
    	for (int i = 0; i < points.size(); i++) {
    		double x1,y1,x2,y2,x,y;//variables: current and adjacent points
    		//for first and last points, special cases due to out of bound array errors
    		if (i == 0) {
    			x1 = points.get(points.size()-1)[0];
    			y1 = points.get(points.size()-1)[1];
    			x2 = points.get(1)[0];
    			y2 = points.get(1)[1];
    		}
    		else if (i == points.size()-1) {
    			x1 = points.get(points.size()-2)[0];
    			y1 = points.get(points.size()-2)[1];
    			x2 = points.get(0)[0];
    			y2 = points.get(0)[1];
    		}
    		else {//just the previous and next points
    			x1 = points.get(i-1)[0];
    			y1 = points.get(i-1)[1];
    			x2 = points.get(i+1)[0];
    			y2 = points.get(i+1)[1];
    		}
    		//current point
    		x = points.get(i)[0];
    		y = points.get(i)[1];
    		
    		double xPrime = x-translateX;//translate the coordinates
    		double yPrime = y-translateY;
    		
    		//secant and segment slopes
    		double slope1 = (y-y1)/(x-x1);
    		double slope = (y2-y1)/(x2-x1);
    		
    		//finds the angle between hyperplane of segment and normal vector to hyperplane
    		double angle1;
    		//vertical or horizontal hyperplane
    		if (y==y1) {
    			angle1 = 0.5*Math.PI;
    			polarInterior.add(new double[] {0,1/yPrime});
    		}
    		else if (x==x1) {
    			angle1 = 0;
    			polarInterior.add(new double[] {1/xPrime,0});
    		}	
    		//other case
    		else {
    			double perpslope = -1/slope1;//slope of normal vector
    			angle1 = Math.atan(perpslope);
    			double hyperX = (slope1*xPrime-yPrime)/(slope1+(1/slope1));//intersection of normal vector and hyperplane
    			if (hyperX < 0) {//when angle > pi/2
    				angle1 += Math.PI;
    			}
    			
    			//support: distance from origin to tangent hyperplane
        		double supportInit = Math.abs(slope1*xPrime-yPrime)/(Math.pow((Math.pow(slope1,2)+1), 0.5));
        		polarInterior.add(new double[] {Math.cos(angle1)/supportInit, Math.sin(angle1)/supportInit});//scaled down to satisfy def of polar
    		}
    		
    		//same procedure as above but with secant slope
    		double angle;
    		if (y2 == y1) {
    			angle = 0.5*Math.PI;
    			polarInterior.add(new double[] {0, 1/yPrime});
    		}
    		else if (x2 == x1) {
    			angle = 0.0;
    			polarInterior.add(new double[] {1/xPrime, 0});
    		}
    		else {
    			double perpSlope = -1/slope;
    			angle = Math.atan(perpSlope);
    			double hyperX = (slope*xPrime-yPrime)/(slope+(1/slope));
    			if (hyperX < 0) {
    				angle += Math.PI;
    			}
    			
    			double support = Math.abs(slope*xPrime-yPrime)/(Math.pow((Math.pow(slope, 2)+1), 0.5));
    			polarInterior.add(new double[] {Math.cos(angle)/support, Math.sin(angle)/support});
    		}
    	}
    	
    	//accounts for any invalid points on the polar interior body
    	for (int i = 0; i < polarInterior.size(); i++) {
    		if (Double.isNaN(polarInterior.get(i)[0]) || Double.isNaN(polarInterior.get(i)[1])){
    			polarInterior.remove(i);
    			i--;
    		}
    	}
    	//returns the polar of the translated body
    	return polarInterior;
    }
    
    public static void findSantalo() {
    	//this part deletes any translations that cause the origin to be out of the body
    	//because if the origin is not in the body, then the polar is unbounded
    	for (int i = 0; i < inside.size(); i++) {
    		ArrayList<double[]> translateBody = new ArrayList<double[]>();
    		for (int j = 0; j < points.size(); j++) {
    			double newX = points.get(j)[0]+inside.get(i)[0];
    			double newY = points.get(j)[1]+inside.get(i)[1];
    			translateBody.add(new double[] {newX,newY});
    		}
    		if (!(isInside(translateBody, new double[] {0,0}))) {//if origin is not in translated body
    			inside.remove(i);
    			i--;
    		}
    	}
    	
    	ArrayList<double[]> goodCopy = new ArrayList<double[]>();
    	//procedure to find interior and boundary of Santalo Regions
    	double intArea = 0.0;
    	double ogArea = shoelace(points);
    	for (int k = 0; k < inside.size(); k++) {
    		double currX = inside.get(k)[0];
    		double currY = inside.get(k)[1];
    		
    		intArea = shoelace(getPolarInterior(currX,currY));
    		//condition of Santalo Regions
    		if (ogArea*intArea <= Math.pow(Math.PI, 2)*distort) {
    			goodCopy.add(new double[] {currX,currY});
    			System.out.println(currX+" "+currY);
    		}
    	}
    	SantaloPoints = makeHullPresorted(goodCopy);//convex hull body to ensure there are no outliers
    }
    
    public static void findInterior() {
    	//variables for extrema of x and y
    	double minX = points.get(0)[0];
    	double minY = points.get(0)[1];
    	double maxX = points.get(0)[0];
    	double maxY = points.get(0)[1];
    	
    	//finds the actual extrema of coords
    	for (int i = 1; i < points.size(); i++) {
    		double x1 = points.get(i)[0];
    		double y1 = points.get(i)[1];
    		
    		//if any of the current coords exceed the max or mins, reset the variables
    		if (x1 < minX)
    			minX = x1;
    		if (x1 > maxX)
    			maxX = x1;
    		if (y1 < minY)
    			minY = y1;
    		if (y1 > maxY)
    			maxY = y1;
    	}
    	
    	double meshX;
    	double meshY;
    	if (Math.log(distort-initialD)/Math.log(10)<=-3) {
    		meshX = 250*Math.exp(0.7*(-(Math.floor(Math.log(distort-initialD)/Math.log(10)))-3));
    		meshY = 250*Math.exp(0.7*(-(Math.floor(Math.log(distort-initialD)/Math.log(10)))-3));
    	}
    	else {
    		meshX = 100;
    		meshY = 100;
    	}
    	//loops through points with x and y coords strictly between the extrema
    	for (int i = (int)(minX*meshX)+1; i < (int)(maxX*meshX); i++) {
    		for (int j = (int)(minY*meshY)+1; j< (int)(maxY*meshY); j++) {
    			double x = i/meshX;
    			double y = j/meshY;
    			
    			if (isInside(points, new double[] {x,y})) {//references isInside method
    				inside.add(new double[] {x,y});
    				System.out.println(x+ " "+y);
    			}
    		}	
    	}
    }
    
    /**
     * This method deletes any points that makes the body not a function
     * @param coords
     */
    public static ArrayList<double[]> removeRepeat(ArrayList<double[]> coords){
    	for (int i = 0; i < coords.size()-1; i++) {
    		if (coords.get(i)[0] == coords.get(i+1)[0]) {//if two consecutive x's are the same, not a function
    			coords.remove(i);
    			i--;
    		}
    	}
    	return coords;
    }
    
    /**
     * This method helps to delete any repeat of y values that cause the body to be concave.
     * @param coords
     */
    public static ArrayList<double[]> removeRepeat2(ArrayList<double[]> coords){
		for (int i = 0; i < coords.size()-1; i++) {
			ArrayList<double[]> repeatArr = new ArrayList<double[]>();//generates a list of coordinates with repeating y's
			double[] keep = new double[2];
			if (coords.get(i)[1] == coords.get(i+1)[1]) {
    			while (i < coords.size()-1 && coords.get(i)[1] == coords.get(i+1)[1]) {
    				repeatArr.add(coords.get(i));
    				i++; 
    			}
    			repeatArr.add(coords.get(i));
    			keep = repeatArr.get(repeatArr.size()/2);//keep the middle coord
    			
    			i -= (repeatArr.size()-1);
    			for (int j = 0; j < repeatArr.size();j++) {
    				coords.remove(i);//remove the other repeats	
    			}
    			coords.add(i, keep);
			}
		}
    	return coords;
    }
    
    /**
     * This method is used to sort a set of coordinates in increasing x order.
     * @param coords
     */
    public static ArrayList<double[]> sort(ArrayList<double[]> coords){
    	for (int i = 0; i < coords.size()-1; i++) {//loop through coordinates
    		for (int j = 0; j < coords.size()-i-1; j++) {
    			//if the current x is greater than the next x, swap
    			if (coords.get(j)[0] > coords.get(j+1)[0]) {
    				double tempX = coords.get(j)[0];
    				double tempY = coords.get(j)[1];
    				double plus1X = coords.get(j+1)[0];
    				double plus1Y = coords.get(j+1)[1];
    				
    				coords.set(j, new double[] {plus1X,plus1Y});//reset current and next
    				coords.set(j+1, new double[] {tempX,tempY});
    			}
    		}
    	}
    	return coords;
    }
    
    /**
     * This method is used to sort a set of coordinates in decreasing order.
     * @param coords
     */
    public static ArrayList<double[]> reverseSort(ArrayList<double[]> coords){
    	for (int i = 0; i < coords.size()-1; i++) {
    		for (int j = 0; j < coords.size()-i-1; j++) {//loops through points
    			//if current x is less than next x, swap
    			if (coords.get(j)[0] < coords.get(j+1)[0]) {
    				double tempX = coords.get(j)[0];
    				double tempY = coords.get(j)[1];
    				double plus1X = coords.get(j+1)[0];
    				double plus1Y = coords.get(j+1)[1];
    				
    				coords.set(j, new double[] {plus1X,plus1Y});//reset current and next x
    				coords.set(j+1, new double[] {tempX,tempY});
    			}
    		}
    	}
    	return coords;
    }
    
    public static ArrayList<double[]> fixValues(ArrayList<double[]>coords) {
    	//variables for extrema of x and y coords
    	double minX = coords.get(0)[0];
    	double maxX = coords.get(0)[0];
    	double minY = coords.get(0)[1];
    	double maxY = coords.get(0)[1];
    	 
    	//variables for when these extrema occur
    	int minXi = 0;
    	int maxXi = 0;
    	int minYi = 0;
    	int maxYi = 0;
    	
    	for (int i = 0; i < coords.size(); i++){
    		double x1 = coords.get(i)[0];
    		double y1 = coords.get(i)[1];
    		
    		//if any of the current coords exceed the max or mins, reset the variables
    		if (x1 < minX) {
    			minX = x1;
    			minXi = i;
    		}
    		if (x1 > maxX) {
    			maxX = x1;
    			maxXi = i;
    		}
    		if (y1 < minY) {
    			minY = y1;
    			minYi = i;
    		}
    		if (y1 > maxY) {
    			maxY = y1;
    			maxYi = i;
    		}
    	}
    	
    	//sorts the first half in increasing order
    	//sorts the second half in decreasing order
    	ArrayList<double[]> firstHalf = new ArrayList<double[]>();
    	ArrayList<double[]> secondHalf = new ArrayList<double[]>();
    	for(int i = minXi; i < maxXi; i++) {//first half is up to index with largest x
    		firstHalf.add(coords.get(i));
    	}
    	for (int i = maxXi; i < coords.size(); i++) {
    		secondHalf.add(coords.get(i));
    	}
    	if (minXi > 0) {//when minXi is not 0, add the points that are from 0 to minXi
    		for (int i = 0; i < minXi; i++) {
    			secondHalf.add(coords.get(i));
    		}
    	}
    	firstHalf = sort(firstHalf);//sorts first half increasing
    	secondHalf = reverseSort(secondHalf);//sorts second half decreasing
    	
    	//adds the points back to the original body
    	coords = new ArrayList<double[]>();
    	for (int i = 0; i < firstHalf.size(); i++) {
    		coords.add(firstHalf.get(i));
    	}
    	for(int i = 0; i < secondHalf.size(); i++) {
    		coords.add(secondHalf.get(i));
    	}
    	return coords;
    }
    
    /**
     * This is the method to find the convex hull of a set of points. It implements
     * Andrew's monotone chain algorithm to carry out this task.
     * @param points
     */
	public static ArrayList<double[]> makeHullPresorted(ArrayList<double[]> points) {
		if (points.size() <= 1)
			return new ArrayList<>(points);
		
		ArrayList<double[]> upperHull = new ArrayList<double[]>();
		for (double[] p : points) {
			while (upperHull.size() >= 2) {
				double[] q = upperHull.get(upperHull.size() - 1);
				double[] r = upperHull.get(upperHull.size() - 2);
				if ((q[0] - r[0]) * (p[1] - r[1]) >= (q[1] - r[1]) * (p[0] - r[0]))
					upperHull.remove(upperHull.size() - 1);
				else
					break;
			}
			upperHull.add(p);
		}
		upperHull.remove(upperHull.size() - 1);
		
		ArrayList<double[]> lowerHull = new ArrayList<double[]>();
		for (int i = points.size() - 1; i >= 0; i--) {
			double[] p = points.get(i);
			while (lowerHull.size() >= 2) {
				double[] q = lowerHull.get(lowerHull.size() - 1);
				double[] r = lowerHull.get(lowerHull.size() - 2);
				if ((q[0] - r[0]) * (p[1] - r[1]) >= (q[1] - r[1]) * (p[0] - r[0]))
					lowerHull.remove(lowerHull.size() - 1);
				else
					break;
			}
			lowerHull.add(p);
		}
		lowerHull.remove(lowerHull.size() - 1);
		
		if (!(upperHull.size() == 1 && upperHull.equals(lowerHull)))
			upperHull.addAll(lowerHull);
		return upperHull;
	}

	/**
	 * This method helps to determine if a point is inside a certain body or not
	 * @param coords
	 * @param coord
	 */
	public static boolean isInside(ArrayList<double[]> coords, double[]coord) {
		int numGreater = 0;
		//x and y coord of desired coord
		double x = coord[0];
		double y = coord[1];
		
		//loops through to determine which segment contains a point with x coordinate of desired x
		for (int i = 0; i < coords.size(); i++) {
			double currX, currY, nextX, nextY;//variables for current and next coords
			if (i == coords.size()-1) {//next from last is first
				nextX = coords.get(0)[0];
				nextY = coords.get(0)[1];
			}
			else {//general next coord
				nextX = coords.get(i+1)[0];
				nextY = coords.get(i+1)[1];
			}
			//current coord
			currX = coords.get(i)[0];
			currY = coords.get(i)[1];
			
			//min and max of the two to determine the range
			double minX = Math.min(currX, nextX);
			double maxX = Math.max(currX, nextX);
			
			//if x lies between currX and nextX
			if (x > minX && x < maxX) {
				//slope of segment on the body
				double slope = (nextY-currY)/(nextX-currX);
				//the value of y at the same x coord as the desired x
				double projectY = currY + slope*(x-currX);
				if (y > projectY)
					numGreater++;
			}
			if (x == currX) {//if x is one of the endpoints
				if (y > currY)
					numGreater++;
			}
		}
		//if y is greater than only one of the y's which lie on the body and have the same x, it is on the interior of the body
		//can be interpreted as a vertical ray towards infinity intersecting the body an odd number of times
		if (numGreater%2 == 1 && numGreater <= 2)
			return true;
		return false;
	}
	
    /**
     * Helper methods to transform JPanel coordinates into Cartesian coordinates.
     * JPanel coordinates work this way: the top left corner is the origin and 
     * the bottom right corner is the (length, width) coordinate on the plot.
     */
    public static double transformX (double coord, double width) {
    	double newCoord = coord - (width/2);
    	return newCoord;
    }
    
    public static double transformY (double coord, double height) {
    	double newCoord = (height/2) - coord;
    	return newCoord;
    }
    
    /**
     * Helper methods to un-transform coordinates for drawing.
     */
    public static double unX(double coord, double width) {
    	double ogCoord = coord + (width/2);
    	return ogCoord;
    }
    
    public static double unY(double coord, double height) {
    	double ogCoord = (height/2) - coord;
    	return ogCoord;
    }
}

