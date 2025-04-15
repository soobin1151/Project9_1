package com.cookandroid.project9_1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static int LINE = 1, CIRCLE = 2, RECT = 3;
    static int curShape = LINE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CHECK", "onCreate 시작됨!");
        EdgeToEdge.enable(this);
        setContentView(new MyGraphicView(this));
        setTitle("간단 그림판");
    }
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add(0,1,0,"선 추가");
        menu.add(0,2,0,"원 추가");
        menu.add(0,3,0,"사각형 추가");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 1:
                curShape = LINE;
                return true;
            case 2:
                curShape = CIRCLE;
                return true;
            case 3:
                curShape = RECT;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    static class Shape {
        int shapeType;
        int startX, startY, stopX, stopY;

        Shape(int shapeType, int startX, int startY, int stopX, int stopY) {
            this.shapeType = shapeType;
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }
    }
    public class MyGraphicView extends View{
        int startX = -1, startY = -1,stopX = -1, stopY = -1;
        public MyGraphicView(Context context){
            super(context);
        }
        ArrayList<Shape> shapeArrayList = new ArrayList<>();

        public boolean onTouchEvent(MotionEvent event){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    this.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    shapeArrayList.add(new Shape(curShape,startX,startY,stopX,stopY));
                    this.invalidate();
                    break;
            }
            return true;
        }
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);

            for (Shape shape : shapeArrayList) {
                drawShape(canvas, paint, shape);
            }

            if (startX != -1 && startY != -1 && stopX != -1 && stopY != -1 &&
                    (startX != stopX || startY != stopY)) {
                Shape previewShape = new Shape(curShape, startX, startY, stopX, stopY);
                drawShape(canvas, paint, previewShape);
            }
        }
        private void drawShape(Canvas canvas, Paint paint, Shape shape) {
            switch (shape.shapeType) {
                case LINE:
                    canvas.drawLine(shape.startX, shape.startY, shape.stopX, shape.stopY, paint);
                    break;
                case CIRCLE:
                    int radius = (int) Math.sqrt(
                            Math.pow(shape.stopX - shape.startX, 2)
                                    + Math.pow(shape.stopY - shape.startY, 2));
                    canvas.drawCircle(shape.startX, shape.startY, radius, paint);
                    break;
                case RECT:
                    canvas.drawRect(shape.startX, shape.startY, shape.stopX, shape.stopY, paint);
                    break;
            }
        }
    }
}