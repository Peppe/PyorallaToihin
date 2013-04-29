package fi.valonia.pyorallatoihin.views.company;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ChameleonTheme;

import fi.valonia.pyorallatoihin.Messages;
import fi.valonia.pyorallatoihin.PyorallaToihinUI;
import fi.valonia.pyorallatoihin.components.Spacer;
import fi.valonia.pyorallatoihin.data.Employee;
import fi.valonia.pyorallatoihin.data.Season;
import fi.valonia.pyorallatoihin.interfaces.ISystemService;
import fi.valonia.pyorallatoihin.views.company.UserForm.FormSubmit;

@SuppressWarnings("serial")
public class EmployeeRow extends CssLayout {

	private final Employee employee;
	private final CompanyScreen companyScreen;
	private final Button[] buttons = new Button[8];
	private CssLayout controls;

	ClickListener clickListener = new ClickListener() {
		private static final long serialVersionUID = -3633715053483874698L;

		@Override
		public void buttonClick(ClickEvent event) {
			int day = (Integer) event.getButton().getData();
			toggleDay(day);
		}
	};
	private Label kmTotal;

	public EmployeeRow(Employee employee, CompanyScreen companyScreen) {
		this.employee = employee;
		this.companyScreen = companyScreen;
		setStyleName("employee-row");
		paintRow();
		addLayoutClickListener(new LayoutClickListener() {

			@Override
			public void layoutClick(LayoutClickEvent event) {
				System.out.println("hello world");
				if (event.getClickedComponent() instanceof Button) {
					return;
				}
				EmployeeRow.this.companyScreen
						.selectEmployeeRow(EmployeeRow.this);
			}
		});
	}

	private void paintRow() {
		CssLayout row = new CssLayout();
		row.addStyleName("employee-row-row");
		row.setWidth("1000px");
		Label name = new Label(employee.getName());
		name.setSizeUndefined();
		name.addStyleName("name");
		Label sport = new Label(PyorallaToihinUI.get().getMessages()
				.getString(employee.getSport().toString()));
		sport.addStyleName("sport");
		Label km = new Label(String.valueOf(employee.getDistance()));
		km.setStyleName("km-label");

		name.setWidth(null);
		sport.setWidth(null);
		km.setWidth(null);

		row.addComponent(name);
		row.addComponent(sport);
		row.addComponent(km);

		for (int i = 0; i < 8; i++) {
			Button button = new Button(null, clickListener);
			button.setData(i);
			button.addStyleName("employee-day");
			button.addStyleName((employee.getDays()[i]) ? "yes" : "no");
			if (employee.getDays()[i]) {
				button.setIcon(new ThemeResource("img/check.png"));
			}
			row.addComponent(button);
			buttons[i] = button;
		}
		kmTotal = new Label();
		kmTotal.setWidth("90px");
		kmTotal.setStyleName("km-label");
		updateTotal(false);
		row.addComponent(kmTotal);
		addComponent(row);
	}

	public Employee getEmployee() {
		return employee;
	}

	public boolean toggleToday() {
		int today = getTodayNumber();
		if (today >= 0 && today <= 7) {
			toggleDay(today);
			return employee.getDays()[today];
		}
		return false;
	}

	/**
	 * toggles the state of the day
	 * 
	 * @param day
	 *            0 to 7 (0 is first day)
	 */
	private boolean toggleDay(int day) {
		PyorallaToihinUI ui = PyorallaToihinUI.get();
		employee.getDays()[day] = !employee.getDays()[day];
		ui.getCompanyService().updateEmployeeDays(employee);
		// removeAllComponents();
		// paintRow();
		int daysMarked = 0;
		for (boolean b : employee.getDays()) {
			if (b) {
				daysMarked++;
			}
		}
		if (daysMarked > 5 && employee.getDays()[day]) {
			Notification
					.show(ui.getMessages().getString(
							Messages.max_five_days_to_stats),
							Type.TRAY_NOTIFICATION);
		}
		if (getTodayNumber() == day) {
			companyScreen.todayToggled();
		}
		if (employee.getDays()[day]) {
			buttons[day].setStyleName("employee-day");
			buttons[day].addStyleName("yes");
			buttons[day].setIcon(new ThemeResource("img/check.png"));
		} else {
			buttons[day].setStyleName("employee-day");
			buttons[day].addStyleName("no");
			buttons[day].setIcon(null);
		}
		updateTotal(true);
		return employee.getDays()[day];
	}

	public boolean isTodayMarked() {
		int today = getTodayNumber();
		if (today >= 0 && today <= 7) {
			return employee.getDays()[today];
		} else {
			return false;
		}
	}

	private int getTodayNumber() {
		Date currentDate = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(currentDate);
		ISystemService systemService = PyorallaToihinUI.get()
				.getSystemService();
		Season currentSeason = systemService.getCurrentSeason();
		Date startDate = currentSeason.getStartDate();
		int day = (int) (currentDate.getTime() - startDate.getTime())
				/ (1000 * 60 * 60 * 24);
		return day;
	}

	public void updateTotal(boolean notify) {
		double total = 0;
		boolean[] days = employee.getDays();
		for (int i = 0; i < 8; i++) {
			if (days[i] == true) {
				total++;
			}
		}
		if (total > 5) {
			total = 5;
		}
		kmTotal.setValue(String.valueOf(new BigDecimal(total)
				.multiply(new BigDecimal(employee.getDistance()))
				.setScale(1, RoundingMode.HALF_UP).toString()));
		if (notify) {
			companyScreen.updateStats();
		}
	}

	public void select() {
		addStyleName("selected");
		if (controls == null) {
			controls = new CssLayout();
			controls.setWidth("60px");
			controls.addStyleName("employee-controls");
			controls.addComponent(createDeleteButton());
			controls.addComponent(createModifyButton());
			addComponent(controls);
		}
	}

	private Button createDeleteButton() {

		// TODO icon
		Button button = new Button();
		button.setIcon(new ThemeResource("img/Delete.png"));
		button.setStyleName(ChameleonTheme.BUTTON_BORDERLESS);
		// TODO translate
		button.setDescription("Poista");
		button.setWidth("24px");
		button.setHeight("24px");
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5457045290015477705L;

			@Override
			public void buttonClick(ClickEvent event) {
				VerticalLayout layout = new VerticalLayout();
				final Window window = new Window(
						"Poista " + employee.getName(), layout);
				// TODO localize
				Label label = new Label(
						"Oletko varma että haluat poistaa käyttäjän <b>"
								+ employee.getName() + "</b>", ContentMode.HTML);
				HorizontalLayout buttons = new HorizontalLayout();
				Button accept = new Button("Hyväksy", new ClickListener() {
					private static final long serialVersionUID = -5255880234005606761L;

					@Override
					public void buttonClick(ClickEvent event) {
						PyorallaToihinUI ui = PyorallaToihinUI.get();
						ui.getCompanyService().deleteEmployee(employee);
						companyScreen.employeeDeleted(EmployeeRow.this);
						ui.removeWindow(window);
					}
				});
				Button decline = new Button("Peruuta", new ClickListener() {
					private static final long serialVersionUID = 4460770398255567444L;

					@Override
					public void buttonClick(ClickEvent event) {
						PyorallaToihinUI.get().removeWindow(window);

					}
				});
				buttons.addComponent(accept);
				buttons.addComponent(decline);
				buttons.setSpacing(true);
				layout.addComponent(label);
				layout.addComponent(new Spacer(null, "40px"));
				layout.addComponent(buttons);
				layout.setMargin(true);
				window.setModal(true);
				window.setWidth("400px");
				PyorallaToihinUI.get().addWindow(window);
			}
		});
		return button;
	}

	private Button createModifyButton() {

		// TODO icon
		Button button = new Button();
		button.setIcon(new ThemeResource("img/Modify.png"));
		button.setStyleName(ChameleonTheme.BUTTON_BORDERLESS);
		// TODO translate
		button.setDescription("Muokkaa");
		button.setWidth("24px");
		button.setHeight("24px");
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5457045290015477705L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Window window = new Window("Muokkaa "
						+ employee.getName());

				FormSubmit formSubmit = new FormSubmit() {
					@Override
					public void submit(Employee employee) {
						companyScreen.updateEmployeeDetails(employee);
						window.close();
					}
				};
				final UserForm newUserLayout = new UserForm(employee,
						formSubmit);
				newUserLayout.setMargin(true);
				window.setContent(newUserLayout);
				window.addShortcutListener(new ShortcutListener(null,
						KeyCode.ESCAPE, null) {
					private static final long serialVersionUID = -7666402117367991543L;

					@Override
					public void handleAction(Object sender, Object target) {
						window.close();
					}
				});
				window.addShortcutListener(new ShortcutListener(null,
						KeyCode.ENTER, null) {
					private static final long serialVersionUID = -4974329151498727901L;

					@Override
					public void handleAction(Object sender, Object target) {
						newUserLayout.submit();
					}
				});
				window.setModal(true);
				PyorallaToihinUI.get().addWindow(window);
				window.setWidth(null);
				window.setHeight("150px");
				window.focus();
			}
		});
		return button;
	}

	public void unselect() {
		removeStyleName("selected");
		if (controls != null) {
			removeComponent(controls);
			controls = null;
		}
	}
}
