package fi.valonia.pyorallatoihin;

import java.util.Locale;

import com.vaadin.annotations.Theme;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Root;

import fi.valonia.pyorallatoihin.backend.CompanyService;
import fi.valonia.pyorallatoihin.data.Company;
import fi.valonia.pyorallatoihin.interfaces.ICompanyService;
import fi.valonia.pyorallatoihin.interfaces.ISystemService;
import fi.valonia.pyorallatoihin.mock.SystemServiceMock;
import fi.valonia.pyorallatoihin.views.company.CompanyScreen;
import fi.valonia.pyorallatoihin.views.login.LoginScreen;

@Theme("pyoralla-toihin")
public class PyorallaToihinRoot extends Root {

    String token = null;
    int userId = -1;
    String language = null;

    ICompanyService companyService = new CompanyService();
    ISystemService systemService = new SystemServiceMock();
    Messages messages;

    @Override
    protected void init(WrappedRequest request) {
        String fragment = request.getBrowserDetails().getUriFragment();
        parseFragment(fragment);
        addListener(new FragmentChangedListener() {

            public void fragmentChanged(FragmentChangedEvent event) {
                parseFragment(event.getFragment());
                openFragmentView();
            }
        });
        openFragmentView();
    }

    private void openFragmentView() {
        setLanguage(language);
        setCaption(messages.getString(Messages.header_topic));
        Company company = companyService.findCompany(token);
        if (company != null) {
            if (userId == -1) {
                showCompany(company);
            } else {
                showCompany(company, userId);
            }
        } else {
            setContent(new LoginScreen(this));
        }
    }

    public void showCompany(Company company) {
        token = company.getToken();
        setFragment(false);
        CompanyScreen screen = new CompanyScreen(this, company);
        setContent(screen);
    }

    public void showCompany(Company company, int userId) {
        token = company.getToken();
        setFragment(false);
        CompanyScreen screen = new CompanyScreen(this, company, userId);
        setContent(screen);
    }

    public void setUserId(int userId) {
        this.userId = userId;
        setFragment(false);
    }

    public void setLanguage(String language) {
        Locale locale;
        if (language != null && language.equals("SE")) {
            this.language = language;
            locale = new Locale("sv", "SE");
        } else if (language != null && language.equals("EN")) {
            this.language = language;
            locale = new Locale("en", "GB");
        } else {
            this.language = null;
            locale = new Locale("fi", "FI");
        }
        setLocale(locale);
        messages = new Messages(locale);
        setFragment(true);
    }

    /**
     * - possible uri formats #ABCDE - company #ABCDE/12 - company, user
     * #ABCDE/12/se - company, user, language #ABCDE/se - company, language #se
     * - language
     */
    private void parseFragment(String fragmentString) {
        token = null;
        userId = -1;
        language = null;
        if (fragmentString != null && !fragmentString.isEmpty()) {
            String[] fragments = fragmentString.split("/");
            String tokenMatcher = "[A-Za-z0-9]{5}";
            String userIdMatcher = "[0-9]*";
            String languageMatcher = "FI|fi|SE|se|EN|en";
            for (String fragment : fragments) {
                if (fragment.matches(tokenMatcher)) {
                    token = fragment;
                }
                if (fragment.matches(userIdMatcher)) {
                    try {
                        userId = Integer.parseInt(fragment);
                    } catch (NumberFormatException nfe) {
                    }
                }
                if (fragment.matches(languageMatcher)) {
                    language = fragment;
                }
            }
        }
        if (token != null) {
            System.out.println("token: " + token);
        }
        if (userId != -1) {
            System.out.println("userId: " + userId);
        }
        if (language != null) {
            System.out.println("language: " + language);
        }
    }

    private void setFragment(boolean fireEvent) {
        StringBuilder fragment = new StringBuilder();
        if (token != null) {
            fragment.append(token);
        }
        if (userId != -1) {
            if (fragment.length() != 0) {
                fragment.append('/');
            }
            fragment.append(userId);
        }
        if (language != null
                && (language.equals("SE") || language.equals("EN"))) {
            if (fragment.length() != 0) {
                fragment.append('/');
            }
            fragment.append(language);
        }
        setFragment(fragment.toString(), fireEvent);
    }

    public ICompanyService getCompanyService() {
        return companyService;
    }

    public ISystemService getSystemService() {
        return systemService;
    }

    public Messages getMessages() {
        return messages;
    }
}
