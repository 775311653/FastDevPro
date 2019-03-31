package com.mohe.fastdevpro.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;

import java.net.URI;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 大灯泡 on 2015/11/4.
 * 识别url的textView
 */
@SuppressLint("AppCompatCustomView")
public class HttpTextView extends TextView {
    //测试文字
    public static String testText =
            "兴业企业移动银行业务开通：\n" +
                    "1、成为兴业银行企业网上银行客户（已是企业网银老客户可跳过此步）。\n" +
                    "2、开通企业网银的“移动银行”功能（已开通客户可跳过此步）：\n" +
                    "（1）专业版企业网银客户可在线开通，请管理员登录企业网银，在“移动银行-移动银行功能开通-移动银行开关设置”中，将“企业移动银行”开关设置为“开通”，主管授权后生效。\n" +
                    "（2）单人版、双人版企业网银客户，请至账户行开通。\n" +
                    "3、以上步骤完成后，企业客户可在账户行免费申领蓝牙网盾。企业申领蓝牙网盾所需材料请另咨询账户行。\n" +
                    "详情及使用指南下载请点击（http://www.cib.com.cn/cn/e-banking/mcorporate/guide.html）。\n" +
                    "www.baidu.com），哈哈哈www.google.com)。\n" +
                    "点击（http://www.cib.com.cn/cn/）\n" +
                    "";
//            "1.测试测试测试google.cn测试曹娥U去我如\n" + "2.侧首IU包宿123124 baidu.com报道锁人副I我去额555\n"
//                    + "3.博啊us豆腐啊哦I吧安静哦.博爱us都I人.dsaboauo www.weiju.ba/xx2/b54\n"
//                    + "4.这是一个测试哟http://www.baidu.com；,这是测试哟\n"
//                    + "5.测试测试啊是的赴欧 我们的网址是：qq.164701463.net测试测试哟\n"
//                    + "6.的撒发吧额听歌：https://xx.125.com 654987打飞机阿伯I安\n"
//                    + "7.把儿童的方向：ftp://4399.com多发生部位，大师傅帮你\n"
//                    + "8.这次是个多网址哟 www.baidu.com），哈哈哈www.google.com)。垃圾都是泪放假啊是的佛I 8264.com\n"
//                    + "9.你敢相信这是一个测试？www.baidu.com/?html=12354bhb35&ask=dasoiubao\n"
//                    + "10.这是一个下载地址哟 www.baidu.com/img/xxxx.jpg\n"
//                    + "11.baidu.com这个地址在开头\n"
//                    + "12.这个地址在末尾baidu.com\n"
//                    + "13.这是文字加地址加哈哈baba.ba微笑掉地赴澳IU发qq.com微笑";

    /*
     * 正则文本
     * ((http|ftp|https)://)(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\&%_\./-~-]*)?|(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\&%_\./-~-]*)?
     * */
    private String pattern ="";
    // 创建 Pattern 对象
    Pattern r =getUrlPattern();
    //            Pattern.compile(pattern);
    // 现在创建 matcher 对象
    Matcher m;
    //记录网址的list
    LinkedList<String> mStringList;
    //记录该网址所在位置的list
    LinkedList<UrlInfo> mUrlInfos;
    int flag=Spanned.SPAN_POINT_MARK;

    private boolean needToRegionUrl = true;//是否开启识别URL，默认开启

    public HttpTextView(Context context) {
        this(context, null);
    }

    public HttpTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HttpTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mStringList = new LinkedList<>();
        mUrlInfos = new LinkedList<>();
    }
    public void setUrlText(CharSequence text) {
        try {
            if (needToRegionUrl) {
                SpannableStringBuilderAllVer stringBuilderAllVer = recognUrl(text);
                super.setText(stringBuilderAllVer);
                this.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                super.setText(text);
            }
        }catch (Exception e){
            LogUtils.e(e.getMessage());
        }
    }

    public boolean getIsNeedToRegionUrl() {
        return needToRegionUrl;
    }

    public void setOpenRegionUrl(boolean needToRegionUrl) {
        this.needToRegionUrl = needToRegionUrl;
    }

    private SpannableStringBuilderAllVer recognUrl(CharSequence text) {
        try {
            mStringList.clear();
            mUrlInfos.clear();

            CharSequence contextText;
            CharSequence clickText;
            text = text == null ? "" : text;
            //以下用于拼接本来存在的spanText
            SpannableStringBuilderAllVer span = new SpannableStringBuilderAllVer(text);
            ClickableSpan[] clickableSpans = span.getSpans(0, text.length(), ClickableSpan.class);
            if (clickableSpans.length > 0) {
                int start=0;
                int end=0;
                for (int i=0;i<clickableSpans.length;i++){
                    start=span.getSpanStart(clickableSpans[0]);
                    end=span.getSpanEnd(clickableSpans[i]);
                }
                //可点击文本后面的内容页
                contextText = text.subSequence(end, text.length());
                //可点击文本
                clickText = text.subSequence(start,
                        end);
            } else {
                contextText = text;
                clickText = null;
            }
            m = r.matcher(contextText);
            //匹配成功
            while (m.find()) {
                //得到网址数
                UrlInfo info = new UrlInfo();
                info.start = m.start();
                info.end = m.end();
                mStringList.add(m.group());
                mUrlInfos.add(info);
            }
            return jointText(clickText, contextText);
        }catch (Exception e){
            LogUtils.e(e.getMessage());
            return new SpannableStringBuilderAllVer();
        }
    }

    /** 拼接文本 */
    private SpannableStringBuilderAllVer jointText(CharSequence clickSpanText,
                                                   CharSequence contentText) {
        SpannableStringBuilderAllVer spanBuilder;
        if (clickSpanText != null) {
            spanBuilder = new SpannableStringBuilderAllVer(clickSpanText);
        } else {
            spanBuilder = new SpannableStringBuilderAllVer();
        }
        if (mStringList.size() > 0) {
            //只有一个网址
            if (mStringList.size() == 1) {
                String preStr = contentText.toString().substring(0, mUrlInfos.get(0).start);
                spanBuilder.append(preStr);
                String url = mStringList.get(0);
                spanBuilder.append(url, new URLClick(url), flag);
                String nextStr = contentText.toString().substring(mUrlInfos.get(0).end);
                spanBuilder.append(nextStr);
            } else {
                //有多个网址
                for (int i = 0; i < mStringList.size(); i++) {
                    if (i == 0) {
                        //拼接第1个span的前面文本
                        String headStr =
                                contentText.toString().substring(0, mUrlInfos.get(0).start);
                        spanBuilder.append(headStr);
                    }
                    if (i == mStringList.size() - 1) {
                        //拼接最后一个span的后面的文本
                        spanBuilder.append(mStringList.get(i), new URLClick(mStringList.get(i)),
                                flag);
                        String footStr = contentText.toString().substring(mUrlInfos.get(i).end);
                        spanBuilder.append(footStr);
                    }
                    if (i != mStringList.size() - 1) {
                        //拼接两两span之间的文本
                        spanBuilder.append(mStringList.get(i), new URLClick(mStringList.get(i)), flag);
                        String betweenStr = contentText.toString()
                                .substring(mUrlInfos.get(i).end,
                                        mUrlInfos.get(i + 1).start);
                        spanBuilder.append(betweenStr);
                    }
                }
            }
        } else {
            spanBuilder.append(contentText);
        }

        return spanBuilder;
    }

    // all domain names
    private static final String[] ext = {
            "top", "com.cn", "com", "net", "org", "edu", "gov", "html","int", "mil", "cn", "tel", "biz", "cc", "tv", "info",
            "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "coop", "aero", "ad", "ae", "af",
            "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd",
            "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz",
            "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr", "cu", "cv", "cx",
            "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es", "et", "ev", "fi",
            "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gh", "gi", "gl", "gm", "gn", "gp",
            "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "in", "io",
            "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw",
            "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md",
            "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mv", "mw", "mx", "my", "mz",
            "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz", "om", "qa", "pa",
            "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re", "ro", "ru", "rw",
            "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st",
            "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt",
            "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn", "vu", "wf", "ws",
            "ye", "yu", "za", "zm", "zr", "zw"
    };

    private static Pattern getUrlPattern(){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < ext.length; i++) {
            sb.append(ext[i]);
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        //不匹配中文特殊字符
        String cnSymbol="[\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b]";
        // final pattern str
        String sPattern = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z\\.\\-]+\\.)?[a-zA-Z0-9\\-]+\\." + sb.toString() + "(:[0-9]{1,5})?))((/[a-zA-Z0-9\\./[^"+cnSymbol+"]\\?'\\+&%\\$#=~_\\-]*)|([^\\u4e00-\\u9fa5\\s0-9a-zA-Z\\./,;()"+cnSymbol+"\\?'\\+&%\\$#=~_\\-]*))";
        // Log.v(TAG, "pattern = " + sPattern);
        Pattern pattern = Pattern.compile(sPattern);
        return pattern;
    }

    //------------------------------------------定义-----------------------------------------------
    class UrlInfo {
        public int start;
        public int end;
    }

    class URLClick extends ClickableSpan {
        private String text;

        public URLClick(String text) {
            this.text = text;
        }

        @Override
        public void onClick(View widget) {
//            Toast.makeText(widget.getContext(),text,Toast.LENGTH_SHORT).show();
            try {
                String url="";
                if (!text.startsWith("http")){
                    url="http://"+text;
                }else url=text;
                Uri uri= Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ActivityUtils.startActivity(intent);
            }catch (Exception e){
                LogUtils.e(e.getMessage());
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(0xff517fae);
            ds.setUnderlineText(false);
        }
    }
}