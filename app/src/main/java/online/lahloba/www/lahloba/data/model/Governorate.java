package online.lahloba.www.lahloba.data.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Governorate extends ExpandableGroup<CityItem> {

    public Governorate(String title, List<CityItem> items) {
        super(title, items);
    }
}
