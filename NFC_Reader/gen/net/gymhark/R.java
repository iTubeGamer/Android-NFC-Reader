/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package net.gymhark;

public final class R {
    public static final class attr {
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int metaButtonBarButtonStyle=0x7f010001;
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int metaButtonBarStyle=0x7f010000;
    }
    public static final class color {
        public static final int black_overlay=0x7f050000;
    }
    public static final class dimen {
        /**  Default screen margins, per the Android Design guidelines. 

         Example customization of dimensions originally defined in res/values/dimens.xml
         (such as screen margins) for screens with more than 820dp of available width. This
         would include 7" and 10" devices in landscape (~960dp and ~1280dp respectively).
    
         */
        public static final int activity_horizontal_margin=0x7f060000;
        public static final int activity_vertical_margin=0x7f060001;
    }
    public static final class drawable {
        public static final int ic_launcher=0x7f020000;
        public static final int nfc_icon=0x7f020001;
    }
    public static final class id {
        public static final int LinearLayout2=0x7f0a0000;
        public static final int action_settings=0x7f0a000f;
        public static final int btnZuweisen=0x7f0a0006;
        public static final int etId=0x7f0a0005;
        public static final int etSchueler=0x7f0a0003;
        public static final int imageView1=0x7f0a000b;
        public static final int progressBar1=0x7f0a000e;
        public static final int tab1=0x7f0a0001;
        public static final int tab2=0x7f0a0007;
        public static final int textView1=0x7f0a0002;
        public static final int textView2=0x7f0a0004;
        public static final int textView3=0x7f0a0009;
        public static final int tvId=0x7f0a000a;
        public static final int tvSchueler=0x7f0a0008;
        public static final int tvSchuelerZeit=0x7f0a000d;
        public static final int tvgetSchuelerId=0x7f0a000c;
    }
    public static final class layout {
        public static final int activity_main=0x7f030000;
        public static final int activity_write_to_tag=0x7f030001;
        public static final int tab2=0x7f030002;
    }
    public static final class menu {
        public static final int main=0x7f090000;
        public static final int write_to_tag=0x7f090001;
    }
    public static final class string {
        public static final int action_settings=0x7f070002;
        public static final int app_name=0x7f070000;
        public static final int dummy_button=0x7f07000f;
        public static final int dummy_content=0x7f070010;
        public static final int error_detected=0x7f070004;
        public static final int error_writing=0x7f070006;
        public static final int hello_world=0x7f070013;
        public static final int id=0x7f07000b;
        public static final int maxmuster=0x7f07000c;
        public static final int musterid=0x7f07000d;
        public static final int musterzeit=0x7f070015;
        public static final int name=0x7f070003;
        public static final int nfcid=0x7f070001;
        public static final int ok_detection=0x7f070007;
        public static final int ok_writing=0x7f070005;
        public static final int ready=0x7f070011;
        public static final int schueler=0x7f07000a;
        public static final int start=0x7f070016;
        public static final int stop=0x7f070017;
        public static final int tagschuelerzuweisung=0x7f070008;
        public static final int title_activity_write__nfc=0x7f07000e;
        public static final int title_activity_write_to_tag=0x7f070012;
        public static final int write=0x7f070009;
        public static final int zeit=0x7f070014;
    }
    public static final class style {
        /** 
        Base application theme, dependent on API level. This theme is replaced
        by AppBaseTheme from res/values-vXX/styles.xml on newer devices.

    

            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.

        

        Base application theme for API 11+. This theme completely replaces
        AppBaseTheme from res/values/styles.xml on API 11+ devices.

    
 API 11 theme customizations can go here. 

        Base application theme for API 14+. This theme completely replaces
        AppBaseTheme from BOTH res/values/styles.xml and
        res/values-v11/styles.xml on API 14+ devices.
    
 API 14 theme customizations can go here. 
         */
        public static final int AppBaseTheme=0x7f080000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f080001;
        public static final int ButtonBar=0x7f080003;
        public static final int ButtonBarButton=0x7f080004;
        public static final int FullscreenActionBarStyle=0x7f080005;
        public static final int FullscreenTheme=0x7f080002;
    }
    public static final class xml {
        public static final int nfc_tech_filter=0x7f040000;
    }
    public static final class styleable {
        /** 
         Declare custom theme attributes that allow changing which styles are
         used for button bars depending on the API level.
         ?android:attr/buttonBarStyle is new as of API 11 so this is
         necessary to support previous API levels.
    
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_metaButtonBarButtonStyle net.gymhark:metaButtonBarButtonStyle}</code></td><td></td></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_metaButtonBarStyle net.gymhark:metaButtonBarStyle}</code></td><td></td></tr>
           </table>
           @see #ButtonBarContainerTheme_metaButtonBarButtonStyle
           @see #ButtonBarContainerTheme_metaButtonBarStyle
         */
        public static final int[] ButtonBarContainerTheme = {
            0x7f010000, 0x7f010001
        };
        /**
          <p>This symbol is the offset where the {@link net.gymhark.R.attr#metaButtonBarButtonStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name net.gymhark:metaButtonBarButtonStyle
        */
        public static final int ButtonBarContainerTheme_metaButtonBarButtonStyle = 1;
        /**
          <p>This symbol is the offset where the {@link net.gymhark.R.attr#metaButtonBarStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name net.gymhark:metaButtonBarStyle
        */
        public static final int ButtonBarContainerTheme_metaButtonBarStyle = 0;
    };
}
