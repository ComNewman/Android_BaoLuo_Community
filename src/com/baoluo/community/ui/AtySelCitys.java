package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baoluo.community.ui.wheel.ArrayWheelAdapter;
import com.baoluo.community.ui.wheel.OnWheelChangedListener;
import com.baoluo.community.ui.wheel.WheelView;

public class AtySelCitys extends AtyBase {
	
	private String[] countries;
	private String[][] cities;
	private String province,province_city;
	static int province_oldValue,province_newValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sel_citys);
		init();
	}
	private void init() {
		 WheelView wheelView = (WheelView) findViewById(R.id.country);
			countries = new String[] { "北京市", "天津市", "上海市", "重庆市",
					"河北省", "河南省", "云南省", "辽宁省", "黑龙江省", "湖南省",
					"安徽省", "山东省", "新疆省", "江苏省", "浙江省", "江西省",
					"河北省", "广西省", "甘肃省", "山西省", "内蒙古", "陕西省",
					"吉林省", "福建省", "贵州省", "广东省", "青海省", "西藏省",
					"四川省", "宁夏省", "海南省", "台湾省" };
	        wheelView.setVisibleItems(5);
	        wheelView.setCyclic(true);//
	        wheelView.setAdapter(new ArrayWheelAdapter<String>(countries));

	        cities = new String[][] {
	        		//北京
	        		new String[] {"北京"},
	        		//天津
	        		new String[] {"天津"},
	        		//上海
	        		new String[] {"上海"},
	        		//重庆
	        		new String[] {"重庆"},
	        		//河北
	        		new String[] {"承德","张家口","保定","石家庄","邢台","邯郸","衡水","沧州","廊坊","唐山","秦皇岛"},
	        		//河南
	        		new String[] {"郑州","洛阳","商丘","安阳","开封","平顶山","焦作","新乡","鹤壁","濮阳","许昌","漯河","三门峡","信阳","周口","驻马店","济源"},
	        		//云南
	        		new String[] {"昆明","曲靖","玉溪"},
	        		//辽宁
	        		new String[] {"沈阳","大连","鞍山","抚顺","本溪","丹东","锦州","营口","阜新","辽阳","盘锦","铁岭","朝阳","葫芦岛"},
	        		//黑龙江
	        		new String[] {"哈尔滨","齐齐哈尔","鸡西","鹤岗","双鸭山","大庆","伊春","佳木斯","七台河","牡丹江","黑河","绥化","大兴安岭"},
	        		//湖南省
	        		new String[] {"长沙","株洲","湘潭","衡阳","邵阳","岳阳","常德","张家界","益阳","郴州","永州","怀化","娄底","湘西"},
	        		//安徽省
	        		new String[] {"合肥","芜湖","蚌埠","淮南","马鞍山","淮北","铜陵","安庆","黄山","滁州","阜阳","宿州","巢湖","六安","亳州","池州","宣城"},
	        		//山东省
	        		new String[] {"济南","青岛","淄博","枣庄","东营","烟台","潍坊","济宁","泰安","威海","日照","莱芜","临沂","德州","聊城","滨州","菏泽"},
	        		//新疆省
	        		new String[] {"乌鲁木齐","克拉玛依","吐鲁番","哈密","昌吉","博尔塔拉","库尔勒","阿克苏","阿图什","喀什","和田","伊犁","塔城","阿勒泰","自治区直辖县级行政单位"},
	        		//江苏省
	        		new String[] {"南京","无锡","徐州","常州","苏州","南通","连云港","淮安","盐城","扬州","镇江","泰州","宿迁"},
	        		//浙江省
	        		new String[] {"杭州","宁波","温州","嘉兴","湖州","绍兴","金华","衢州","舟山","台州","丽水"},
	        		//江西省
	        		new String[] {"南昌","景德镇","萍乡","九江","新余","鹰潭","赣州","吉安","宜春","抚州","上饶"},
	        		//河北省
	        		new String[] {"武汉","黄石","十堰","宜昌","襄樊","鄂州","荆门","孝感","荆州","黄冈","咸宁","随州","恩施","省直辖县级行政单位"},
	        		//广西省
	        		new String[] {"南宁","柳州","桂林","梧州","北海","防城港","钦州","贵港","玉林","百色","贺州","河池","来宾","崇左"},
	        		//甘肃省
	        		new String[] {"兰州","嘉峪关","金昌","白银","天水","武威","张掖","平凉","酒泉","庆阳","定西","陇南","临夏","甘南"},
	        		//山西省
	        		new String[] {"太原","大同","阳泉","长治","晋城","朔州","晋中","运城","忻州","临汾","吕梁"},
	        		//内蒙古
	        		new String[] {"呼和浩特","包头","乌海","赤峰","通辽","鄂尔多斯","呼伦贝尔","巴彦淖尔","乌兰察布","兴安盟","锡林郭勒","阿拉善盟"},
	        		//陕西省
	        		new String[] {"西安","铜川","宝鸡","咸阳","渭南","延安","汉中","榆林","安康","商洛"},
	        		//吉林省
	        		new String[] {"长春","吉林","四平","辽源","通化","白山","松原","白城","延边"},
	        		//福建省
	        		new String[] {"福州","厦门","莆田","三明","泉州","漳州","南平","龙岩","宁德"},
	        		//贵州省
	        		new String[] {"贵阳","六盘水","遵义","安顺","铜仁","兴义","毕节","凯里","都匀"},
	        		//广东省
	        		new String[] {"广州","韶关","深圳","珠海","汕头","佛山","江门","湛江","茂名","肇庆","惠州","梅州","汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮"},
	        		//青海省
	        		new String[] {"广州","韶关","深圳","珠海","汕头","佛山","江门","湛江","茂名","肇庆","惠州","梅州","汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮"},
	        		//西藏
	        		new String[] {"拉萨","昌都","山南","日喀则","那曲","阿里","林芝"},
	        		//四川
	        		new String[] {"成都","自贡","攀枝花","泸州","德阳","绵阳","广元","遂宁","内江","乐山","南充","眉山","宜宾","广安","达州","雅安","巴中","资阳","阿坝","甘孜","凉山"},
	        		//宁夏
	        		new String[] {"银川","石嘴山","吴忠","固原","中卫"},
	        		//海南
	        		new String[] {"海口","三亚","省直辖县级行政单位"},
	        		//台湾
	        		new String[] {"台北","高雄","基隆","台中","台南","新竹","嘉义"},
	        		};
	        
	        final WheelView city = (WheelView) findViewById(R.id.city);
	        city.setVisibleItems(5);

	        wheelView.addChangingListener(new OnWheelChangedListener() {
				public void onChanged(WheelView wheel, int oldValue,int newValue) {
					province_oldValue = newValue;
					city.setAdapter(new ArrayWheelAdapter<String>(cities[newValue]));
					city.setCurrentItem(cities[newValue].length / 2);
					province = countries[newValue];
					province_city = cities[newValue][cities[newValue].length / 2];
				}
			});
	        
	        city.addChangingListener(new OnWheelChangedListener() {
	        	public void onChanged(WheelView wheel, int oldValue, int newValue) {
					province_city = cities[province_oldValue][newValue];
	        	}
	        });
	        //设置选择器默认指向的城市
	        wheelView.setCurrentItem(1);
		
	}
	public void Button_OK(View v){
    	Toast.makeText(this, province + "—" + province_city, 0).show();
    	Intent i = new Intent();
    	i.putExtra("province", province);
		i.putExtra("city", province_city);
		setResult(RESULT_OK, i);
		finish();
    }

}
