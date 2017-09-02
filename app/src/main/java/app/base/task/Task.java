package app.base.task;

import java.util.Random;
/***
 * 任务基类
 * @author Administrator
 *
 */
public class Task implements Cloneable{

	public static final Random random = new Random();
	public int seq;

	public int getSeq() {
		return seq;
	}

	public Task setSeq(int seq) {
		this.seq = seq;
		return this;
	}

	public String tag;

	public Task setTag(int tag) {
		
		this.tag = tag+"";
		return this;
	}

	public String identity;

	public String getId() {
		return identity;
	}

	public void setId(String id) {
		this.identity = id;
	}

	public Task(Task backRunnable) {
		this.identity = backRunnable.identity;		
	}

	public Task() {
		super();
		
		this.identity = random.nextInt() + "";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

	Compt compt;

	public Compt getCompt() {
		return compt;
	}

	public Task setCompt(Compt compt) {
		this.compt = compt;
		return this;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

}
