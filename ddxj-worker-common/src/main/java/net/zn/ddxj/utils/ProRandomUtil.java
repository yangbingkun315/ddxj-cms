package net.zn.ddxj.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

/**
 * 概率随机数
 * @author GZF
 *
 */
public class ProRandomUtil {
	
	private static final Map<Integer, Integer> keyChanceMap = new HashMap<Integer, Integer>();

	static {
		keyChanceMap.put(1,40000);
		keyChanceMap.put(2,30000);
		keyChanceMap.put(3,20000);
		keyChanceMap.put(4,9999);
		keyChanceMap.put(88,1);
	}
	

	/**
	 * 返回奖励金随机数
	 * @param keyChanceMap 包含数字概率的map
	 * @return
	 */
    public static BigDecimal probabilityRandom (){
        List<Integer> list = new ArrayList<>();
        Integer item = null;
        for (int i = 0; i < 10; i++) {
            item = chanceSelect(keyChanceMap);
            list.add(item);
        }
        Random rand = new Random();
        int num = rand.nextInt(10);
        Integer returnNum = list.get(num);
        if(returnNum!=88){
        	double bonus = Math.random()*1+returnNum;
        	BigDecimal inviterBonus = new BigDecimal(bonus).setScale(2,BigDecimal.ROUND_FLOOR);
        	return inviterBonus;
        }else{
        	return new BigDecimal(list.get(num));
        }
    }

    public static Integer chanceSelect(Map<Integer, Integer> keyChanceMap) {
        if (keyChanceMap == null || keyChanceMap.size() == 0)
            return null;

        Integer sum = 0;
        for (Integer value : keyChanceMap.values()) {
            sum += value;
        }
        Integer rand = new Random().nextInt(sum) + 1;
        for (Entry<Integer, Integer> entry : keyChanceMap.entrySet()) {
            rand -= entry.getValue();
            // 选中
            if (rand <= 0) {
                Integer item = entry.getKey();
                return item;
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
    	
    	for (int i = 0; i < 1000000; i++) {
    		BigDecimal a = ProRandomUtil.probabilityRandom();
    		System.out.println(a);
    		if(a.compareTo(new BigDecimal("88.00"))==0){
    			System.out.println("i:"+i+"   8888888");
    			break;
    		}
		}
	}
	
}
