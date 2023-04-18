package cn.edu.hbut.weatherbesttest.gson;

import com.google.gson.annotations.SerializedName;

//天气数据组成1：城市基本信息(名称city，天气id，更新时间loc)
public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
