package app.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;

import android.os.Parcel;
import android.os.Parcelable;
/***
 * 封装jsonarray实体
 * @author Administrator
 *
 */
public class Entity implements Parcelable{
	public String tag;
	public JSONArray jsonarray;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int type = -1;
	public static final int json = 0;
	public boolean cache;
	public Entity(JSONArray jsonarray) {
		super();
		this.jsonarray = jsonarray;
		type=0;
	}
	public Entity(String id) {
		super();
		fieldContents.put("id", id);
	}
	public Entity(Entity obj) {
		super();
		this.tag = obj.tag;
		this.fieldContents = obj.fieldContents;
	}

	public Map<String, Object> fieldContents = new HashMap<String, Object>();
	public String get(String key){
		Object rlt = fieldContents.get(key);
		return rlt==null?null:rlt.toString();
	}
	public Entity(Map<String, Object> fieldContents) {
		super();
		this.fieldContents = fieldContents;
	}
	public Entity() {
		// TODO Auto-generated constructor stub
	}
	public Object getObject(String key){		
		return fieldContents.get(key);
	}
	public String getString(String key){
		
		if(fieldContents.get(key)!=null){
			return fieldContents.get(key).toString();
		}
		throw new IllegalStateException("value empty");
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldContents == null) ? 0 : fieldContents.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		String tpid = getId();
		if (fieldContents == null) {
			if (other.fieldContents != null)
				return false;
		} else {
			if(fieldContents.get(tpid)==null){
				return super.equals(obj);
			}
			if (!fieldContents.get(tpid).equals(other.fieldContents.get(tpid)))
			return false;}
		return true;
	}
	public String getId() {
		String tpid = "Id";
		Object Id = fieldContents.get("Id");
		if(Id==null){
			tpid = "id";
		}
		return tpid;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}	public static final Creator<Entity> CREATOR = new Creator<Entity>() {
		public Entity createFromParcel(Parcel source) {
			return new Entity(source.readHashMap(Entity.class
					.getClassLoader()));
		}

		@Override
		public Entity[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Entity[size];
		}
	};
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
		
			Logs.i("--- size "+fieldContents.size());
		for(Entry<String,Object> en: fieldContents.entrySet()){
			Logs.i("--- key value "+en.getKey()+" "+en.getValue());
		}
		dest.writeMap(fieldContents);
		}
	
	
}
