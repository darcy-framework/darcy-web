package com.redhat.darcy.web;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.web.stubs.FakeScreenshotTakingBrowser;

import com.google.common.jimfs.Jimfs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

@RunWith(JUnit4.class)
public class TakeScreenshotTest {
    @Test
    public void shouldWriteDataToPath() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        FakeScreenshotTakingBrowser browser = new FakeScreenshotTakingBrowser(data);

        FileSystem fileSystem = Jimfs.newFileSystem();
        Path path = fileSystem.getPath("example.png");

        browser.takeScreenshot(path);

        assertTrue("Expected file to be created", Files.exists(path));
        assertThat("Expected the correct data to be written to the file",
                Files.readAllBytes(path), equalTo(data));
    }

    @Test
    public void shouldWriteDataToPathWithParentDirectories() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        FakeScreenshotTakingBrowser browser = new FakeScreenshotTakingBrowser(data);

        FileSystem fileSystem = Jimfs.newFileSystem();
        Path path = fileSystem.getPath("parent/folder/example.png");

        browser.takeScreenshot(path);

        assertTrue("Expected parent directories to be created",
                Files.exists(path.getParent()));
        assertTrue("Expected file to be created", Files.exists(path));
        assertThat("Expected the correct data to be written to the file",
                Files.readAllBytes(path), equalTo(data));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = DarcyException.class)
    public void shouldThrowDarcyExceptionWhenAnIOExceptionOccurs() {
        FakeScreenshotTakingBrowser browser = new FakeScreenshotTakingBrowser(null);

        Path path = mock(Path.class);
        when(path.getParent()).thenThrow(IOException.class);
        browser.takeScreenshot(path);
    }
}
