package fi.valonia.pyorallatoihin.interfaces;

import java.util.List;

import fi.valonia.pyorallatoihin.data.Season;

public interface ISystemService {

    public List<Season> getSeasons();

    public Season getCurrentSeason();

    public void setCurrentSeason(Season season);

}
