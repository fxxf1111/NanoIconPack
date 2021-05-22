集成步骤：

1，将图标放入：app/src/......./drawbale-nodpi
2，app/..res/xml/appfilter.xml       <!-- 桌面启动器 自动适配用 -->
3，app/..res/xml/drawable.xml         <!-- 主动替换图标展示列表 -->

[不需要]app/..res/values/icon_pack.xml    <!-- app内部展示用 -->
[不需要]将大图标放入：app/src/......./mipmap-nodpi (仅仅在app内点击图标显示大图标预览用，图标太多建议不要添加)




如何添加动态日历图标？

部分启动器支持部分日历APP（比如谷歌日历）的按日更新图标功能，我们可以为其添加支持。

步骤基本与添加普通图标一致，只是在第一步的命名和最后一步修改appfilter.xml不同。下面就讲解这两步的做法。

    准备从1日到31日的31枚日历图标（不能少，否则无效），依次命名为calendar_d1.png~calendar_d31.png（也可以是其他命名，但名末的数字序号必须有）。

    修改appfilter.xml：

    <!-- The whole 31 icons are required, or it doesn't work. -->
    <calendar
        component="ComponentInfo{com.google.android.calendar/com.android.calendar.AllInOneActivity}"
        prefix="calendar_d" />

