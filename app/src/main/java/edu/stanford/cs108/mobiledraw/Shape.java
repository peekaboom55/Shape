package edu.stanford.cs108.mobiledraw;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;

import androidx.core.content.ContextCompat;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;
import static androidx.core.graphics.drawable.IconCompat.getResources;
import static java.lang.Math.abs;


import java.util.List;

class Shape {

    public static final int GOTO = 1;
    public static final int PLAY = 2;
    public static final int HIDE = 3;
    public static final int SHOW = 4;
    public static final int ON_CLICK = 5;
    public static final int ON_ENTER = 6;
    public static final int ON_DROP = 7;

    static int idx = 1;

    private String name;    //default: "shape#"

    private String possessionArea;    //page name

    private RectF bound;    //bounding rect (x,y,width,height)

    public float top, bottom, left, right, width, height;

    private String imgName;

    private String text = "";
    private int textSize = 20;

    private boolean hidden = false;    //default: false

    private boolean movable = false;    //default: false

    private Script shapeScript;


    class Script {
        int trigger;
        String shapeName;    //for ON_DROP
        List<Action> actions;

        class Action {
            int action;
            String name;
        }
    }

    public Shape(String imgName, RectF boundRec) {

        if (imgName.length() == 0) {
            //System.out.println("No image input.");
            this.imgName = "";

        } else {
            this.name = "shape" + idx;
            this.imgName = imgName;

            this.bound = boundRec;

            this.left = bound.left;
            this.right = bound.right;
            this.top = bound.top;
            this.bottom = bound.bottom;

            this.width = right - left;
            this.height = top - bottom;

            this.shapeScript = new Script<>(imgName); //prong to change
        }


    }

    public Shape(String text, RectF boundRec) {

        this.name = "shape" + idx;
        this.imgName = text;

        this.bound = boundRec;

        this.left = bound.left;
        this.right = bound.right;
        this.top = bound.top;
        this.bottom = bound.bottom;

        this.width = right - left;
        this.height = top - bottom; //not sure if text shape has a bound or just a point?????

        this.shapeScript = new Script<>(imgName); //prong to change
    }

    //   public Shape(String text, float x1, float x2, float y1, float y2) {}
    // Do we want a constructor that takes in coordinates?????


    // Set
    public void setBound(RectF newBound) {
        bound = newBound;
        top = newBound.top;
        bottom = newBound.bottom;
        right = newBound.right;
        left = newBound.left;
    }

    public void setBoundByCoord(float newTop, float newBottom, float newRight, float newLeft) {
        top = newTop;
        bottom = newBottom;
        right = newRight;
        left = newLeft;

        bound = new RectF(left, top, right, bottom);
    }

    public void setShapeName(String newName) {
        name = newName;
    }

    public void setPossessionArea(String possessionArea) {
        this.possessionArea = possessionArea;
    }

    public void setHidden(boolean hiddenStatus) {
        hidden = hiddenStatus;
    }

    public void setMovable(boolean movableStatus) {
        hidden = movableStatus;
    }

    public void setTextSize(int newTextSize) {
        textSize = newTextSize;
    }


    // Get
    public float getBottom() {
        return bottom;
    }

    public float getTop() {
        return top;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getHeight() {
        height = top - bottom;
        return height;
    }

    public float getWidth() {
        width = right - left;
        return width;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isMovable() {
        return movable;
    }

    public static int getIdx() {
        return idx;
    }

    public String getText() {
        return text;
    }

    public RectF getBound() {
        return bound;
    }

    public Script getShapeScript() {
        return shapeScript;
    }

    public String getName() {
        return name;
    }

    public String getImgName() {
        return imgName;
    }

    public int getTextSize() {
        return textSize;
    }

    public String getPossessionArea() {
        return possessionArea;
    }


    // Draw Functions
    public void draw(Canvas canvas) {

        if (text.length() != 0) {
            drawText(canvas, textSize);

        } else if (imgName.length() != 0) {
            drawImg(canvas);
        } else {
            drawRect(canvas); //if(imgName.length() == 0) draw a grey boundary rect
        }

    }

    private Bitmap original;

    Context ctx;

    public Shape ( Context context) {
        ctx = context;
    }

    private void drawImg(Canvas canvas) {
        //draw bitmap
        if (imgName == "carrot") {
            original = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.carrot);
        } else if (imgName == "carrot2") {
            original = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.carrot2);
        }else if(imgName =="death"){
            original = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.death);
        }else if(imgName =="duck"){
            original = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.duck);
        }else if(imgName =="fire"){
            original = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.fire);
        }else if(imgName =="mystic"){
            original = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.mystic);
        }

        //Dont know how to pass variable into resource hence the hideous code?????
        //Also not sure if the context thing is correct?????
        canvas.drawBitmap(original, null, bound, null);


    }



    private void drawText(Canvas canvas, int textSize) {
        Paint textPaint = new Paint(); //not sure if this can be set as default????
        textPaint.setStyle(FILL);
        textPaint.setStrokeWidth(12.0f);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        canvas.drawText(text, left, bottom, textPaint);

    }

    private void drawRect(Canvas canvas) {
        Paint boundOutline = new Paint();
        boundOutline.setStyle(STROKE);
        boundOutline.setStrokeWidth(5.0f);//not sure if width is set as default???
        boundOutline.setColor(Color.GRAY);

        canvas.drawRect(bound, boundOutline);

    }

}