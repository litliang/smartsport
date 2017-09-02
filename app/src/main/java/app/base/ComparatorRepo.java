package app.base;

import java.util.Comparator;
/***
 * string 比较器
 * @author Administrator
 *
 */
public class ComparatorRepo {
 public final static Comparator<String> keyString = new Comparator<String>() {

		@Override
		public int compare(String object1, String object2) {
			// TODO Auto-generated method stub
			if(object1==null&&object2==null){
				return 0;
			}else if(object1==null||object2==null){
				return -1;
			}else {
				return object1.compareTo(object2);				
			}
		}

	};
}
