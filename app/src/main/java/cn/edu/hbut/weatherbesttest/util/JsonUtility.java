package cn.edu.hbut.weatherbesttest.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.hbut.weatherbesttest.db.City;
import cn.edu.hbut.weatherbesttest.db.District;
import cn.edu.hbut.weatherbesttest.db.Province;
import cn.edu.hbut.weatherbesttest.gson.Weather;

//因为省份城市区县xml数据较为简单，所以使用JSONObject解析
public class JsonUtility {

    public static  boolean handleProvinceResponse(String response){
        //解析和处理服务器返回的省级数据
        /*  https://guolin.tech/api/china
        [{"id":1,"name":"北京"},{"id":2,"name":"上海"},{"id":3,"name":"天津"},{"id":4,"name":"重庆"},{"id":5,"name":"香港"},{"id":6,"name":"澳门"},{"id":7,"name":"台湾"},{"id":8,"name":"黑龙江"},{"id":9,"name":"吉林"},{"id":10,"name":"辽宁"},{"id":11,"name":"内蒙古"},{"id":12,"name":"河北"},{"id":13,"name":"河南"},{"id":14,"name":"山西"},{"id":15,"name":"山东"},{"id":16,"name":"江苏"},{"id":17,"name":"浙江"},{"id":18,"name":"福建"},{"id":19,"name":"江西"},{"id":20,"name":"安徽"},{"id":21,"name":"湖北"},{"id":22,"name":"湖南"},{"id":23,"name":"广东"},{"id":24,"name":"广西"},{"id":25,"name":"海南"},{"id":26,"name":"贵州"},{"id":27,"name":"云南"},{"id":28,"name":"四川"},{"id":29,"name":"西藏"},{"id":30,"name":"陕西"},{"id":31,"name":"宁夏"},{"id":32,"name":"甘肃"},{"id":33,"name":"青海"},{"id":34,"name":"新疆"}]
        */
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for(int i=0; i<allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    public static  boolean handleCityResponse(String response, int provinceId){
        //解析和处理服务器返回的市级数据
        /*  https://guolin.tech/api/china/21
        [{"id":174,"name":"武汉"},{"id":175,"name":"襄阳"},{"id":176,"name":"鄂州"},{"id":177,"name":"孝感"},{"id":178,"name":"黄冈"},{"id":179,"name":"黄石"},{"id":180,"name":"咸宁"},{"id":181,"name":"荆州"},{"id":182,"name":"宜昌"},{"id":183,"name":"恩施"},{"id":184,"name":"十堰"},{"id":185,"name":"神农架"},{"id":186,"name":"随州"},{"id":187,"name":"荆门"},{"id":188,"name":"天门"},{"id":189,"name":"仙桃"},{"id":190,"name":"潜江"}]
         */
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i=0; i<allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    public static  boolean handleDistrictResponse(String response, int cityId){
        //解析和处理服务器返回的区级数据
        /*  http://guolin.tech/api/china/21/174
        [{"id":1330,"name":"武汉","weather_id":"CN101200101"},{"id":1331,"name":"蔡甸","weather_id":"CN101200102"},{"id":1332,"name":"黄陂","weather_id":"CN101200103"},{"id":1333,"name":"新洲","weather_id":"CN101200104"},{"id":1334,"name":"江夏","weather_id":"CN101200105"},{"id":1335,"name":"东西湖","weather_id":"CN101200106"}]
         */
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allDistricts = new JSONArray(response);
                for(int i=0; i<allDistricts.length(); i++){
                    JSONObject districtObject = allDistricts.getJSONObject(i);
                    District district = new District();
                    district.setDistrictName(districtObject.getString("name"));
                    district.setWeatherId(districtObject.getString("weather_id"));
                    district.setCityId(cityId);
                    district.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    public static Weather handleWeatherResponse(String response){
        //将返回的JSON数据解析成Weather实体类
        /*  http://guolin.tech/api/weather?cityid=CN101200101&key=ffbac23d09da45f1991a797f5ad8ddf2
        {"HeWeather":[{"basic":{"cid":"CN101200101","location":"武汉","parent_city":"武汉","admin_area":"湖北","cnty":"中国","lat":"36.05804062","lon":"103.82355499","tz":"+8.00","city":"武汉","id":"CN101200101","update":{"loc":"2023-03-07 01:27","utc":"2023-03-07 01:27"}},"update":{"loc":"2023-03-07 01:27","utc":"2023-03-07 01:27"},"status":"ok","now":{"cloud":"91","cond_code":"100","cond_txt":"晴","fl":"11","hum":"12","pcpn":"0.0","pres":"1015","tmp":"14","vis":"16","wind_deg":"46","wind_dir":"东北风","wind_sc":"2","wind_spd":"7","cond":{"code":"100","txt":"晴"}},"daily_forecast":[{"date":"2023-03-08","cond":{"txt_d":"晴"},"tmp":{"max":"18","min":"3"}},{"date":"2023-03-09","cond":{"txt_d":"晴"},"tmp":{"max":"16","min":"4"}},{"date":"2023-03-10","cond":{"txt_d":"晴"},"tmp":{"max":"20","min":"5"}},{"date":"2023-03-11","cond":{"txt_d":"阴"},"tmp":{"max":"19","min":"6"}},{"date":"2023-03-12","cond":{"txt_d":"阴"},"tmp":{"max":"17","min":"4"}},{"date":"2023-03-13","cond":{"txt_d":"小雨"},"tmp":{"max":"13","min":"3"}}],"aqi":{"city":{"aqi":"60","pm25":"22","qlty":"良"}},"suggestion":{"comf":{"type":"comf","brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"},"sport":{"type":"sport","brf":"较适宜","txt":"天气较好，户外运动请注意防晒。推荐您进行室内运动。"},"cw":{"type":"cw","brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}},"msg":"所有天气数据均为模拟数据，仅用作学习目的使用，请勿当作真实的天气预报软件来使用。"}]}
         */

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

         /*  http://guolin.tech/api/weather?cityid=CN101200101&key=ffbac23d09da45f1991a797f5ad8ddf2
        {"HeWeather":[{"basic":{"cid":"CN101200101","location":"武汉","parent_city":"武汉","admin_area":"湖北","cnty":"中国","lat":"36.05804062","lon":"103.82355499","tz":"+8.00","city":"武汉","id":"CN101200101","update":{"loc":"2023-03-07 01:27","utc":"2023-03-07 01:27"}},"update":{"loc":"2023-03-07 01:27","utc":"2023-03-07 01:27"},"status":"ok","now":{"cloud":"91","cond_code":"100","cond_txt":"晴","fl":"11","hum":"12","pcpn":"0.0","pres":"1015","tmp":"14","vis":"16","wind_deg":"46","wind_dir":"东北风","wind_sc":"2","wind_spd":"7","cond":{"code":"100","txt":"晴"}},"daily_forecast":[{"date":"2023-03-08","cond":{"txt_d":"晴"},"tmp":{"max":"18","min":"3"}},{"date":"2023-03-09","cond":{"txt_d":"晴"},"tmp":{"max":"16","min":"4"}},{"date":"2023-03-10","cond":{"txt_d":"晴"},"tmp":{"max":"20","min":"5"}},{"date":"2023-03-11","cond":{"txt_d":"阴"},"tmp":{"max":"19","min":"6"}},{"date":"2023-03-12","cond":{"txt_d":"阴"},"tmp":{"max":"17","min":"4"}},{"date":"2023-03-13","cond":{"txt_d":"小雨"},"tmp":{"max":"13","min":"3"}}],"aqi":{"city":{"aqi":"60","pm25":"22","qlty":"良"}},"suggestion":{"comf":{"type":"comf","brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"},"sport":{"type":"sport","brf":"较适宜","txt":"天气较好，户外运动请注意防晒。推荐您进行室内运动。"},"cw":{"type":"cw","brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}},"msg":"所有天气数据均为模拟数据，仅用作学习目的使用，请勿当作真实的天气预报软件来使用。"}]}
         */
    }

    public static String handleBackgroundResponse(String response){
        //必应提供了一个图片的接口，我们可以通过访问这个接口来获取每日更新的图片，接口链接如下所示：https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1
        //通过浏览器访问该链接可以得到以下所示数据
        //{"images":[{"startdate":"20230306","fullstartdate":"202303061600","enddate":"20230307","url":"/th?id=OHR.YuanyangChina_ZH-CN7360249295_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.YuanyangChina_ZH-CN7360249295","copyright":"梯田鸟瞰图，元阳，中国 (© AlexGcs/Getty Images)","copyrightlink":"https://www.bing.com/search?q=%E4%B8%AD%E5%9B%BD%E5%85%83%E9%98%B3&form=hpcapt&mkt=zh-cn","title":"彩虹般的风景","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20230306_YuanyangChina%22&FORM=HPQUIZ","wp":true,"hsh":"be72a7063afee4905b50eab9187c5725","drk":1,"top":1,"bot":1,"hs":[]}],"tooltips":{"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}}
        //其中，url标签后的内容即为我们所需图片的地址，通过访问http://cn.bing.com+该地址就可以得到我们想要的图片，例如，上面得到的url为
        ///th?id=OHR.ChurchRock_ZH-CN6926315999_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp
        //原文链接：https://blog.csdn.net/chenxihanhui/article/details/113115019

        String str = "http://cn.bing.com";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String url = object.getString("url");
                return str+url;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
