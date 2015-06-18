package net.bible.android.control.page.window;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import net.bible.android.control.event.EventManager;
import net.bible.android.control.event.EventManagerStub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class WindowRepositoryTest {
	private WindowRepository windowRepository;

	@Before
	public void setUp() throws Exception {
		EventManager eventManager = new EventManagerStub();
		windowRepository = new WindowRepository();
		windowRepository.initialise(eventManager);
	}

	@Test
	public void testGetWindow() throws Exception {
		assertThat(windowRepository.getWindow(1).getScreenNo(), equalTo(1));
	}

	@Test
	public void testGetActiveWindow() throws Exception {
		assertThat(windowRepository.getActiveWindow().getScreenNo(), equalTo(1));
	}

	@Test
	public void testSetActiveWindow() throws Exception {
		Window newWindow = windowRepository.addNewWindow();
		assertThat(windowRepository.getActiveWindow().getScreenNo(), not(equalTo(newWindow.getScreenNo())));
		windowRepository.setActiveWindow(newWindow);
		assertThat(windowRepository.getActiveWindow().getScreenNo(), equalTo(newWindow.getScreenNo()));
	}

	@Test
	public void testMoveWindowToPosition() throws Exception {
		Window originalWindow = windowRepository.getActiveWindow();
		Window newWindow = windowRepository.addNewWindow();
		Window newWindow2 = windowRepository.addNewWindow();
		
		windowRepository.moveWindowToPosition(newWindow, 0);
		assertThat(windowRepository.getWindows(), contains(newWindow, originalWindow, newWindow2));
		
		windowRepository.moveWindowToPosition(newWindow, 1);
		assertThat(windowRepository.getWindows(), contains(originalWindow, newWindow, newWindow2));

		windowRepository.moveWindowToPosition(originalWindow, 2);
		assertThat(windowRepository.getWindows(), contains(newWindow, newWindow2, originalWindow));

		windowRepository.moveWindowToPosition(originalWindow, 0);
		assertThat(windowRepository.getWindows(), contains(originalWindow, newWindow, newWindow2));
	}
	
	@Test
	public void testMoveMissingWindowToPosition() throws Exception {
		Window originalWindow = windowRepository.getActiveWindow();
		Window newWindow = windowRepository.addNewWindow();

		windowRepository.close(newWindow);
		
		windowRepository.moveWindowToPosition(newWindow, 0);
		assertThat(windowRepository.getWindows(), contains(originalWindow));
	}
	
	@Test
	public void testAddAfterDeletedWindowsDifferent() {
		Window newWindow = windowRepository.addNewWindow();
		windowRepository.close(newWindow);
		Window newWindow2 = windowRepository.addNewWindow();
		assertThat(newWindow.equals(newWindow2), not(true));
		assertThat(newWindow.hashCode()==newWindow2.hashCode(), not(true));
	}
}
