package anastasoft.rallyvision.controller.Data.model;

import android.database.Cursor;

import anastasoft.rallyvision.controller.Data.DBHelper;

public class Afericao extends Table {

	private String name;

	private float ratio;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public Afericao(String Name, float ratio) {
		setName(Name);
        setRatio(ratio);
	}

    private void setRatio(float ratio) {
        this.ratio = ratio;
    }



	public String toS() {
		return "(" + getId() + "," + getName() + "," + getRatio() + ")";
	}




    public float getRatio() {
        return ratio;
    }
    public Afericao(Cursor c) {
        this.setId(c.getInt(c.getColumnIndex(DBHelper.KEY_ID)));
        this.setName(c.getString(c
                .getColumnIndex(DBHelper.KEY_AFERICAO_NAME)));
        this.setRatio(c.getFloat(c.getColumnIndex(DBHelper.KEY_AFERICAO_RATIO)));
    }
}
