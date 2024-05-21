package com.lenkeng.videoads.test;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaweizi.marquee.MarqueeTextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenpengchi$
 * @version 1.0
 * @description: TODO
 * @date $ $
 */
public class StyleTextUpdate {
    private Queue<String> logContentQueue = new LinkedList<>();
    private SpanTextView textView2;
    private MarqueeTextView textView3;
    private SpannableStringBuilder accumulatedText = new SpannableStringBuilder();
    private static final String TAG = "StyleTextUpdate";
    private boolean isFillSpaceExecuted = false;
    private   int as=0;
    public StyleTextUpdate(MarqueeTextView textView) {
        this.textView3 = textView;
    }


    public void enqueueLogContent(String content, List<String> styles, boolean isInsideEm, boolean isInsideStrong, DisplayMetrics displayMetrics) {

          as++;
          Log.d("StyleTextUpdate","循环次数"+as);
//        Log.d("StyleTextUpdate","---》内容："+content+"  ---->样式"+styleInfo+" -----s"+content.length());
        // 添加内容到累积文本
        accumulatedText.append(content);
        stylesFlag = new int[12];
        // 应用样式到新添加的文本上
        for (int i = styles.size() - 1; i >= 0; i--) {
            String styleInfo = styles.get(i);
            if (styleInfo == null) {
                continue;
            }
            applyStyles(styleInfo, accumulatedText.length() - content.length(), accumulatedText.length(), displayMetrics);
            int a = accumulatedText.length() - content.length();
            Log.d("StyleTextUpdate", "   index:" + a + "   ---" + accumulatedText.length());
        }
        if (isInsideEm) {
            applyStyles("<em>", accumulatedText.length() - content.length(), accumulatedText.length(), displayMetrics);
        }
        if (isInsideStrong) {
            applyStyles("<strong>", accumulatedText.length() - content.length(), accumulatedText.length(), displayMetrics);

        }

        // 设置到TextView
        textView3.setText(accumulatedText);
        // 设置到TextView之后，检查是否需要填充空格
//        fillSpaceUntilWidthExceeded();
        // 检查是否已经执行过fillSpaceUntilWidthExceeded
//        if (!isFillSpaceExecuted) {
//            // 延迟2秒执行填充空格的逻辑
//            textView3.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (!isFillSpaceExecuted) { // 双重检查锁定，防止并发问题
////                        fillSpaceUntilWidthExceeded();
//                        isFillSpaceExecuted = true; // 设置标志位，表示已执行
//                    }
//                }
//            }, 1500);
//        }
        Log.d("StyleTextUpdate","内容"+accumulatedText);
    }

    public int getLength() {
        return accumulatedText.length();
    }


    int[] stylesFlag = new int[12];

    private void applyStyles(String styleInfo, int startIndex, int endIndex, DisplayMetrics displayMetrics) {
        Log.d("StyleTextUpdate", "传递信息:" + styleInfo + "    s" + startIndex + "  e" + endIndex);

        int i = 0;

        //设置div属性，统计div的信息
        String textViewBackGround = "background:\\s*rgb\\((\\d+),\\s*(\\d+),\\s*(\\d+)\\)";//正则表达
        Pattern backgroundPattern = Pattern.compile(textViewBackGround);
        Matcher backgroundMatcher = backgroundPattern.matcher(styleInfo);
        if (backgroundMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            int r = Integer.parseInt(backgroundMatcher.group(1));
            int g = Integer.parseInt(backgroundMatcher.group(2));
            int b = Integer.parseInt(backgroundMatcher.group(3));
            Log.d(TAG, "RGB:--R" + r + "  --G" + g + " --B" + b);
            textView3.setBackgroundColor(Color.rgb(r, g, b));
        }
        i++;
        //处理宽
        String widthRegex = "width:\\s*([\\d]+(\\.\\d+)?)px;";
        Pattern widthPattern = Pattern.compile(widthRegex);
        Matcher widthMatcher = widthPattern.matcher(styleInfo);
        if (widthMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            int dpSize = Integer.parseInt(widthMatcher.group(1));
            // 转换为dp
            float dpValue = dpSize / (displayMetrics.densityDpi / 500f);
            Log.d(TAG, "widthDP值" + dpValue);
            textView3.setWidth((int) dpValue);
        }
        i++;
        //处理高
        String heightRegex = "(?<!-|[-[:alnum:]])height:\\s*([\\d]+(\\.\\d+)?)px;";
        Pattern heightPattern = Pattern.compile(heightRegex);
        Matcher heightMatcher = heightPattern.matcher(styleInfo);
        if (heightMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            int dpSize = Integer.parseInt(heightMatcher.group(1));
            // 转换为dp
            float dpValue = dpSize / (displayMetrics.densityDpi / 500f);
            Log.d(TAG, "heightDP值" + dpValue);
            textView3.setHeight((int) dpValue);
        }
        i++;
        // 示例：处理字体大小
        String fontSizeRegex = "font-size:\\s*(\\d+)px;";
        Pattern fontSizePattern = Pattern.compile(fontSizeRegex);
        Matcher fontSizeMatcher = fontSizePattern.matcher(styleInfo);

        if (fontSizeMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            int pxSize = Integer.parseInt(fontSizeMatcher.group(1));
            float spSize = pxSize / 18f; // 将像素转换为sp
            accumulatedText.setSpan(new RelativeSizeSpan(spSize), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        i++;
        // 示例：处理颜色
        String colorRegex = "(?<!-|[-[:alnum:]])color:\\s*rgb\\((\\d+),\\s*(\\d+),\\s*(\\d+)\\);";
        Pattern colorPattern = Pattern.compile(colorRegex);
        Matcher colorMatcher = colorPattern.matcher(styleInfo);
        if (colorMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            int r = Integer.parseInt(colorMatcher.group(1));
            int g = Integer.parseInt(colorMatcher.group(2));
            int b = Integer.parseInt(colorMatcher.group(3));
            accumulatedText.setSpan(new ForegroundColorSpan(Color.rgb(r, g, b)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        i++;
        // 示例：处理字体背景颜色
        String bgColorRegex = "background-color:\\s*rgb\\((\\d+),\\s*(\\d+),\\s*(\\d+)\\);";
        Pattern bgColorPattern = Pattern.compile(bgColorRegex);
        Matcher bgColorMatcher = bgColorPattern.matcher(styleInfo);
        if (bgColorMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            int r = Integer.parseInt(bgColorMatcher.group(1));
            int g = Integer.parseInt(bgColorMatcher.group(2));
            int b = Integer.parseInt(bgColorMatcher.group(3));
            accumulatedText.setSpan(new BackgroundColorSpan(Color.rgb(r, g, b)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        i++;
        // 示例：处理删除线
        String textDecorationRegex = "text-decoration:\\s*line-through;";
        Pattern textDecorationPattern = Pattern.compile(textDecorationRegex);
        Matcher textDecorationMatcher = textDecorationPattern.matcher(styleInfo);
        if (textDecorationMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            accumulatedText.setSpan(new StrikethroughSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        i++;
        // 示例：处理下划线
        textDecorationRegex = "text-decoration:\\s*underline;";
        textDecorationPattern = Pattern.compile(textDecorationRegex);
        textDecorationMatcher = textDecorationPattern.matcher(styleInfo);
        if (textDecorationMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            accumulatedText.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        i++;
        // 示例：处理斜体
        String fontStyleRegex = "<em>";
        Pattern fontStylePattern = Pattern.compile(fontStyleRegex);
        Matcher fontStyleMatcher = fontStylePattern.matcher(styleInfo);
        if (fontStyleMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            accumulatedText.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            accumulatedText.setSpan(new StyleSpan(Typeface.BOLD),startIndex,endIndex,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.d("StyleTextUpdate", "已设置em:" + styleInfo);
        }
        i++;
        //处理加粗
        String strongStyleRegex = "<strong>";
        Pattern strongStylePattern = Pattern.compile(strongStyleRegex);
        Matcher strongStyleMatcher = strongStylePattern.matcher(styleInfo);
        if (strongStyleMatcher.find() && stylesFlag[i] == 0) {
            stylesFlag[i] = 1;
            accumulatedText.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.d("StyleTextUpdate", "已设置strong:" + styleInfo);
        }
        i++;
   Log.d("StyleTextUpdate","累计次数"+i);
        //处理在布局中的位置 边距高
        String marginTopRegex ="top:\\s*([\\d]+(\\.\\d+)?)px;";
        Pattern marginTopPattern=Pattern.compile(marginTopRegex);
        Matcher marginTopMatcher=marginTopPattern.matcher(styleInfo);

        if(marginTopMatcher.find()&&stylesFlag[i]==0){
            stylesFlag[i]=1;
            int dpSize=Integer.parseInt(marginTopMatcher.group(1));
            float dpValue = dpSize / (displayMetrics.densityDpi / 490f);
            Log.d("StyleTextUpdate","需要设置的上边距:"+dpValue+"   dpSize"+dpSize);
              setTextViewMarginTop(textView3,(int)dpValue);
        }
        i++;
        //处理布局中的位置 边距left
        String marginLeftRegex="left:\\s*([\\d]+(\\.\\d+)?)px;";
        Pattern marginLeftPattern=Pattern.compile(marginLeftRegex);
        Matcher marginLeftMatcher=marginLeftPattern.matcher(styleInfo);
        if(marginLeftMatcher.find()){
         stylesFlag[i]=1;
         int dpSize=Integer.parseInt(marginLeftMatcher.group(1));
         float dbValue=dpSize/(displayMetrics.densityDpi/520f);
         setTextViewMarginLeft(textView3,(int)dbValue);
        }
        // 根据需要添加更多样式处理逻辑...
    }
    // 添加一个方法来设置TextView的上边距
    private void setTextViewMarginTop(MarqueeTextView textView, int marginTopInPx) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.topMargin = marginTopInPx;
            textView.setLayoutParams(marginLayoutParams);
        }
    }
    //左边距
    private void setTextViewMarginLeft(MarqueeTextView textView, int marginLeftInPx) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.leftMargin = marginLeftInPx;
            textView.setLayoutParams(marginLayoutParams);
        }
    }
    private void fillSpaceUntilWidthExceeded() {
        // 获取TextView的宽度
        int textViewWidth = textView3.getWidth();
        Log.d("lo","wi"+textViewWidth);
        if (textViewWidth <= 0) {
            // 如果TextView的宽度未确定，可能是测量尚未完成，此时不宜进行操作
            return;
        }

        // 使用textView2的paint来估算文本宽度，确保paint的属性与TextView设置的一致
        Paint paint = textView3.getPaint();
        // 假设使用textView2的textSize作为paint的大小，实际情况可能需要调整
        paint.setTextSize(textView3.getTextSize());

        // 计算当前累计文本的总宽度
        float totalTextWidth = paint.measureText(accumulatedText.toString());
        Log.d("StyleTextUpdate","wi"+totalTextWidth+" textwithd"+textViewWidth);
        // 判断是否需要填充空格
        if (totalTextWidth < textViewWidth) {
            // 计算需要填充的空格数量，这里简单假设单个空格的宽度等于' '的宽度
            float spaceWidth = paint.measureText(" ");
            int spacesToAdd = Math.round((textViewWidth - totalTextWidth) / spaceWidth);
            Log.d("StyleTextUpdate","wis"+totalTextWidth+" textwithds"+textViewWidth+"循环次数"+spacesToAdd);
            // 在文本末尾添加空格
            for (int i = 0; i < spacesToAdd; i++) {
                accumulatedText.append("  ");
            }
            // 更新TextView的文本
            textView3.setText(accumulatedText);
        }
    }

}
