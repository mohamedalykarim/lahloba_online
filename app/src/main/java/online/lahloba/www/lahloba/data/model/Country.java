package online.lahloba.www.lahloba.data.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Country extends ExpandableGroup<Governerate> {

    public Country(String title, List<Governerate> items) {
        super(title, items);
    }
}
