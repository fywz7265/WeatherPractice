package cn.edu.hbut.weatherbesttest.gson;

import com.google.gson.annotations.SerializedName;

//天气数据组成3：当前天气
public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
