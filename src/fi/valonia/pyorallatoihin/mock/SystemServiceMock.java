package fi.valonia.pyorallatoihin.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fi.valonia.pyorallatoihin.data.Season;
import fi.valonia.pyorallatoihin.interfaces.ISystemService;

public class SystemServiceMock implements ISystemService, Serializable {
    private static final long serialVersionUID = -2774589976242571798L;
    private final List<Season> seasons = new ArrayList<Season>();
    private Season currentSeason;

    public SystemServiceMock() {
        Calendar calendar = Calendar.getInstance();

        Season season = new Season();
        season.setId(1);
        season.setName("2010");
        calendar.set(2010, 5, 20);
        season.setStartDate(calendar.getTime());
        seasons.add(season);

        season = new Season();
        season.setId(2);
        season.setName("2011");
        calendar.set(2011, 4, 18);
        season.setStartDate(calendar.getTime());
        seasons.add(season);

        season = new Season();
        season.setId(3);
        season.setName("2011 vaihto");
        calendar.set(2012, 3, 2);
        season.setStartDate(calendar.getTime());
        seasons.add(season);
        currentSeason = season;
    }

    @Override
    public List<Season> getSeasons() {
        return seasons;
    }

    @Override
    public Season getCurrentSeason() {
        return currentSeason;
    }

    @Override
    public void setCurrentSeason(Season season) {
        currentSeason = season;

    }

}
