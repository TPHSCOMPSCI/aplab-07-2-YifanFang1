import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

public class Steganography {
    public static void main(String[] args) {
        Picture beach = new Picture ("beach.jpg");
        // beach.explore();

        // Picture copy = testClearLow(beach);
        // copy.explore();

        // Picture copy2 = testSetLow(beach, Color.PINK);
        // copy2.explore();

        // Picture copy3 = revealPicture(copy2);
        // copy3.explore();

        // Picture moon = new Picture("moon-surface.jpg");
        // if (canHide(copy3, moon)) {
        //     Picture copy4 = hidePicture(copy3, moon, 0, 0);
        //     copy4.explore();
        // }
        
        // Picture robot = new Picture("robot.jpg"); 
        // Picture flower1 = new Picture("flower1.jpg");
        // beach.explore();
        // Picture hidden1 = hidePicture(beach, robot, 65, 208); 
        // Picture hidden2 = hidePicture(hidden1, flower1, 280, 110); 
        // hidden2.explore(); 
        // Picture unhidden = revealPicture(hidden2); 
        // unhidden.explore(); 

        // Picture swan = new Picture("swan.jpg"); 
        // Picture swan2 = new Picture("swan.jpg"); 
        // System.out.println("Swan and swan2 are the same: " + isSame(swan, swan2)); 
        // swan = testClearLow(swan); 
        // System.out.println("Swan and swan2 are the same (after clearLow run on swan): " + isSame(swan, swan2));
        
        
        // Picture arch = new Picture("arch.jpg"); 
        // Picture arch2 = new Picture("arch.jpg");
        // Picture koala = new Picture("koala.jpg"); 
        // Picture robot1 = new Picture("robot.jpg");
        // ArrayList<Point> pointList = findDifferences(arch, arch2); 
        // System.out.println("PointList after comparing two identical has a size of " + pointList.size()); 
        // pointList = findDifferences(arch, koala); 
        // System.out.println("PointList after comparing two different sized pictures has a size of " + pointList.size()); 
        // arch2 = hidePicture(arch, robot1, 65, 102); 
        // pointList = findDifferences(arch, arch2);
        // System.out.println("Pointlist after hiding a picture has a size of " + pointList.size()); 
        // arch.show(); 
        // arch2.show(); 

        // Picture hall = new Picture("femaleLionAndHall.jpg"); 
        // Picture robot2 = new Picture("robot.jpg"); 
        // Picture flower2 = new Picture("flower1.jpg"); 
        // Picture hall2 = hidePicture(hall, robot2, 50, 300); 
        // Picture hall3 = hidePicture(hall2, flower2, 115, 275); 
        // hall3.explore(); 
        // if(!isSame(hall, hall3)) { 
        //     Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3)); 
        //     hall4.show();  
        //     Picture unhiddenHall3 = revealPicture(hall3);
        //     unhiddenHall3.show(); 
        // } 

        // beach.explore();
        Picture p = hideText(beach, "HELLO WORLD");
        // p.explore();
        System.out.println(revealText(p));
    }

    public static void clearLow(Pixel p) {
        p.setBlue((int)(p.getBlue()/4)*4);
        p.setRed((int)(p.getRed()/4)*4);
        p.setGreen((int)(p.getGreen()/4)*4);
    }

    public static void setLow(Pixel p, Color c) {
        p.setBlue(((int)(p.getBlue()/4)*4)  +   ((int)(c.getBlue()/128)));
        p.setRed(((int)(p.getRed()/4)*4)    +   ((int)(c.getRed()/128)));
        p.setGreen(((int)(p.getGreen()/4)*4)    +   ((int)(c.getGreen()/128)));
    }

    public static Picture testClearLow(Picture p) {
        for(int i = 0; i < p.getHeight(); i++) {
            for(int j = 0; j < p.getWidth(); j++) {
                clearLow(p.getPixel(j, i));
            }
        }
        return p; 
    }

    public static Picture testSetLow(Picture p, Color c) {
        for(int i = 0; i < p.getHeight(); i++) {
            for(int j = 0; j < p.getWidth(); j++) {
                setLow(p.getPixel(j, i), c);
            }
        }
        return p; 
    }

    public static Picture revealPicture(Picture hidden) {
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] source = hidden.getPixels2D();
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                Color col = source[r][c].getColor();
                Pixel pixel = pixels[r][c];
                
                pixel.setBlue(col.getBlue() % 4 * 64);
                pixel.setRed(col.getRed() % 4 * 64);
                pixel.setGreen(col.getGreen() % 4 * 64);
            }
        }
        return copy;
    }

    public static boolean canHide(Picture source, Picture secret) {
        return (source.getHeight() == secret.getHeight()) && (source.getWidth() == secret.getWidth());
    }

    public static Picture hidePicture(Picture source, Picture secret, int x, int y) {
        Picture image = new Picture(source);
        Pixel[][] pixels = image.getPixels2D();
        Pixel[][] uh = secret.getPixels2D();
        for (int r = 0; r < uh.length; r++) {
            for (int c = 0; c < uh[0].length; c++) {
                Color col = uh[r][c].getColor();
                Pixel p = pixels[r + x][c + y];

                setLow(p, col);
            }
        }
        return image;
    }

    public static boolean isSame(Picture one, Picture two) {
        Pixel[][] pixels = one.getPixels2D();
        Pixel[][] check = two.getPixels2D();
        for (int r = 0; r < check.length; r++) {
            for (int c = 0; c < check[0].length; c++) {
                Color a = pixels[r][c].getColor();
                Color b = check[r][c].getColor();
                if(a.getBlue() != (b.getBlue())) {
                    return false;
                }
                if(a.getRed() != (b.getRed())) {
                    return false;
                }
                if(a.getGreen() != (b.getGreen())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Point> findDifferences(Picture one, Picture two) {
        ArrayList<Point> points = new ArrayList<>();
        
        Pixel[][] pixels = one.getPixels2D();
        Pixel[][] check = two.getPixels2D();

        if(canHide(one, two)) {
            for (int r = 0; r < check.length; r++) {
                for (int c = 0; c < check[0].length; c++) {
                    Color a = pixels[r][c].getColor();
                    Color b = check[r][c].getColor();
                    if(a.getBlue() != (b.getBlue()) || (a.getRed() != (b.getRed())) || (a.getGreen() != (b.getGreen()))) {
                        points.add(new Point(r, c));
                    }
                }
            }
        }
        return points;
    }

    public static Picture showDifferentArea(Picture pic, ArrayList<Point> list) {
        Picture pict = pic;
        Pixel[][] pixels = pict.getPixels2D();

        for(Point p : list) {
            pixels[p.x][p.y].setColor(new Color(128, 0, 128));
        }

        return pict;
    }

    public static ArrayList<Integer> encodeString(String s) { 
        s = s.toUpperCase();
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        ArrayList<Integer> result = new ArrayList<Integer>(); 
        for (int i = 0; i < s.length(); i++) { 
            if (s.substring(i,i+1).equals(" ")) { 
                result.add(27); 
            } 
            else { 
                result.add(alpha.indexOf(s.substring(i,i+1))+1); 
            } 
        } 
        result.add(0); 
        // System.out.println("Encode Works");
        return result; 
    } 

    public static String decodeString(ArrayList<Integer> codes) { 
        String result=""; 
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        for (int i=0; i < codes.size(); i++) { 
            if (codes.get(i) == 27) { 
                result = result + " "; 
            } 
            else { 
                result = result + alpha.substring(codes.get(i)-1,codes.get(i)); 
            } 
        } 
        // System.out.println("Decode Works");
        return result; 
    } 

    private static int[] getBitPairs(int num) { 
        int[] bits = new int[3];   
        int code = num;   
        for (int i = 0; i < 2; i++)   { 
            bits[i] = code % 4; 
            code = code / 4;
        }  
        // for(int b : bits)
        //     System.out.print(b+ " ");
        // System.out.println();
        return bits; 
    } 

    public static Picture hideText(Picture source, String s) {
        ArrayList<Integer> list = encodeString(s);
        ArrayList<int[]> bits = new ArrayList<int[]>();
        for(int num : list) {
            // System.out.println(getBitPairs(num)[0] + " " + getBitPairs(num)[1] + " " + getBitPairs(num)[2]);
            bits.add(getBitPairs(num));
        }

        Picture pic = new Picture(source);
        Pixel[][] px = pic.getPixels2D();
        System.out.println();

        for(int i = 0; i < bits.size(); i++) {
            int j = 0;
            int k = 0;

            Pixel p = px[j][k];

            // System.out.println(p.getRed());
            // System.out.println(p.getGreen());
            // System.out.println(p.getBlue());

            // System.out.println(((p.getRed()/4)*4)    + " " +   bits.get(i)[0]);
            // System.out.println(((p.getGreen()/4)*4)    + " " +   bits.get(i)[1]);
            // System.out.println(((p.getBlue()/4)*4)  + " " +   bits.get(i)[2]);

            p.setRed(((p.getRed()/4)*4)    +   bits.get(i)[0]);
            p.setGreen(((p.getGreen()/4)*4)    +   bits.get(i)[1]);
            p.setBlue(((p.getBlue()/4)*4)  +   bits.get(i)[2]);

            System.out.println(bits.get(i)[0] + " " + bits.get(i)[1] + " " + bits.get(i)[2]);

            System.out.println();

            System.out.println(p.getRed() + " " + p.getGreen() + " " + p.getBlue());

            System.out.println();

            k++;

            if(k == px[0].length) {
                j++;
                k = 0;
            }
        }

        // System.out.println("hideText Works");
        return pic;
    }

    public static String revealText(Picture source) {
        ArrayList<Integer> resultArray = new ArrayList<>();

        Picture pic = new Picture(source);
        Pixel[][] px = pic.getPixels2D();

        System.out.println("Reveal\n");
        for (int a = 0; a < px.length; a++) {
            for (int b = 0; b < px[0].length; b++) {
                Color col = px[a][b].getColor();
                
                System.out.println(col.getRed() + " " + col.getGreen() + " " + col.getBlue() + "\n");

                System.out.println(col.getRed()%4 + " " + col.getGreen()%4 + " " + col.getBlue()%4 + "\n");

                int numS = col.getRed()%4 + col.getGreen()%4 + col.getBlue()%4;
                
                System.out.println(numS);
                
                if(numS == 0) {
                    return decodeString(resultArray);
                }
                resultArray.add(numS);
            }
        }
        return "uh?????????????????????";
    }
}
