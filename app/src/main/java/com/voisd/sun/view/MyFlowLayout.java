package com.voisd.sun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by jm on 2016/6/25.
 */
public class MyFlowLayout extends ViewGroup{
    private int  verticalSpacing = 20;
    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int widthUsed = paddingLeft + paddingRight;
        int heightUsed = paddingTop + paddingBottom;

        int childMaxHeightOfThisLine = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childUsedWidth = 0;
                int childUsedHeight = 0;
                measureChild(child,widthMeasureSpec,heightMeasureSpec);
                childUsedWidth += child.getMeasuredWidth();
                childUsedHeight += child.getMeasuredHeight();

                LayoutParams childLayoutParams = child.getLayoutParams();

                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childLayoutParams;

                childUsedWidth += marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                childUsedHeight += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;

                if (widthUsed + childUsedWidth < widthSpecSize) {
                    widthUsed += childUsedWidth;

                    if (childUsedHeight > childMaxHeightOfThisLine) {
                        childMaxHeightOfThisLine = childUsedHeight;
                    }

                } else {
                    heightUsed += childMaxHeightOfThisLine + verticalSpacing;
                    widthUsed = paddingLeft + paddingRight + childUsedWidth;
                    childMaxHeightOfThisLine = childUsedHeight;
                }


            }

        }

        heightUsed += childMaxHeightOfThisLine;
        setMeasuredDimension(widthSpecSize, heightUsed);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int childStartLayoutX = paddingLeft;
        int childStartLayoutY = paddingTop;

        int widthUsed = paddingLeft + paddingRight;

        int childMaxHeight = 0;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childNeededWidth, childNeedHeight;
                int left, top, right, bottom;

                int childMeasuredWidth = child.getMeasuredWidth();
                int childMeasuredHeight = child.getMeasuredHeight();

                LayoutParams childLayoutParams = child.getLayoutParams();
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childLayoutParams;
                int childLeftMargin = marginLayoutParams.leftMargin;
                int childTopMargin = marginLayoutParams.topMargin;
                int childRightMargin = marginLayoutParams.rightMargin;
                int childBottomMargin = marginLayoutParams.bottomMargin;
                childNeededWidth = childLeftMargin + childRightMargin + childMeasuredWidth;
                childNeedHeight = childTopMargin + childBottomMargin + childMeasuredHeight;

                if (widthUsed + childNeededWidth <= r - l) {
                    if (childNeedHeight > childMaxHeight) {
                        childMaxHeight = childNeedHeight;
                    }
                    left = childStartLayoutX + childLeftMargin;
                    top = childStartLayoutY + childTopMargin;
                    right = left + childMeasuredWidth;
                    bottom = top + childMeasuredHeight;
                    widthUsed += childNeededWidth;
                    childStartLayoutX += childNeededWidth;
                } else {
                    childStartLayoutY += childMaxHeight + verticalSpacing;
                    childStartLayoutX = paddingLeft;
                    widthUsed = paddingLeft + paddingRight;
                    left = childStartLayoutX + childLeftMargin;
                    top = childStartLayoutY + childTopMargin;
                    right = left + childMeasuredWidth;
                    bottom = top + childMeasuredHeight;
                    widthUsed += childNeededWidth;
                    childStartLayoutX += childNeededWidth;
                    childMaxHeight = childNeedHeight;
                }
                child.layout(left, top, right, bottom);
            }
        }
    }

}