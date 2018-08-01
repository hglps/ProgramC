import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Bitmap extends PApplet {

int bitmap[][] = new int[1024][1024];
int y, x, rectSize, maxSize;
PFont font;

boolean lineFlag = false; int linei, linej;
boolean blockFlag = false; float blocki, blockj, blockSize; int blockColor;

int stepTime = 300;

public void setup() {
  
  colorMode(HSB, 360, 100, 100);
  font = createFont("Arial", 30, false);
  textFont(font, 30);
  textAlign(CENTER, CENTER);

  String[] lines = loadStrings("./in");
  int[] yx = PApplet.parseInt(split(lines[0], ' '));
  y = yx[0]; x = yx[1];
  print(y, x, "\n");
  for (int i = 0; i < y; i ++) {
    char[] line = lines[i + 1].toCharArray();
    for (int j = 0; j < x; j ++) {
      bitmap[i][j] = line[j] - '0';
      print(bitmap[i][j]);
    } print("\n");
  }

  rectSize = min(height, width) / max(y, x); maxSize = rectSize * min(y, x);


  thread("animationManager");
}

public void drawBlock(int i, int j) {
  strokeWeight(1);
  stroke(0, 100, 0);
  fill(0, bitmap[i][j] * 100, (1 - bitmap[i][j]) * 100);
  rect(j * rectSize, i * rectSize, rectSize, rectSize);
}

public void draw() {
  background(100);
  for (int i = 0; i < y; i ++)
    for (int j = 0; j < x; j ++)
      drawBlock(i, j);

  if (lineFlag) {
    // fill(300, 100, 100);
    strokeWeight(10);
    stroke(250, 100, 100);
    line(0, linei*rectSize, width, linei*rectSize);
    line(linej*rectSize, 0, linej*rectSize, height);
  }

  if (blockFlag) {
    fill(0, 100, 100);
    textSize(maxSize / (min(y, x) - blockSize) / 2);
    text(str(blockColor), blockj * rectSize, blocki * rectSize);
  }
}

public void animationManager() {
  while (true) {
    delay(1000);
    zoom(0, 0, y - 1, x - 1);
    lineFlag = false;
  }
}

public boolean hasSingle(int li, int lj, int hi, int hj) {
  int aux = bitmap[li][lj];
  for (int i = li; i <= hi; i ++)
    for (int j = lj; j <= hj; j ++)
      if (bitmap[i][j] != aux)
        return(false);
  return(true);
}

public void zoom(int li, int lj, int hi, int hj) {
  if (li > hi || lj > hj) return;
  if (hasSingle(li, lj, hi, hj)) {
    blockFlag = true; blocki = (li + hi) / 2.0f + 0.4f; blockj = (lj + hj) / 2.0f + 0.5f; blockColor = bitmap[li][lj]; blockSize = min(hi - li, hj - lj) + 1;
    delay(stepTime);
    blockFlag = false;
    return;
  }

  int midi = (li + hi) / 2, midj = (lj + hj) / 2;
  linei = midi + 1; linej = midj + 1; lineFlag = true;
  delay(stepTime);
  // print(linei, linej, "\n");
  zoom(li, lj, midi, midj);
  zoom(li, midj + 1, midi, hj);
  zoom(midi + 1, lj, hi, midj);
  zoom(midi + 1, midj + 1, hi, hj);
  lineFlag = false;
}
  public void settings() {  size(900, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Bitmap" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
