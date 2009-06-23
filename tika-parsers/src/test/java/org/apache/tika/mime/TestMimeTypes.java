/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tika.mime;

// Junit imports
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;

/**
 * 
 * Test Suite for the {@link MimeTypes} repository.
 * 
 */
public class TestMimeTypes extends TestCase {

    private MimeTypes repo;

    private static URL u;

    static {
        try {
            u = new URL("http://mydomain.com/x.pdf?x=y");
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        }
    }

    private static final File f = new File("/a/b/c/x.pdf");

    public TestMimeTypes() {
        try {
            repo = TikaConfig.getDefaultConfig().getMimeRepository();
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public void testCaseSensitivity() {
        MimeType type = repo.getMimeType("test.PDF");
        assertNotNull(type);
        assertEquals(repo.getMimeType("test.pdf"), type);
        assertEquals(repo.getMimeType("test.PdF"), type);
        assertEquals(repo.getMimeType("test.pdF"), type);
    }

    public void testLoadMimeTypes() throws MimeTypeException {
        assertNotNull(repo.forName("application/octet-stream"));
        assertNotNull(repo.forName("text/x-tex"));
    }

    /**
     * Tests MIME type determination based solely on the URL's extension.
     */
    public void testGuessMimeTypes() throws Exception {
        assertTypeByName("application/pdf", "x.pdf");
        assertEquals("application/pdf", repo.getMimeType(u).getName());
        assertEquals("application/pdf", repo.getMimeType(f).getName());
        assertTypeByName("text/plain", "x.txt");
        assertTypeByName("text/html", "x.htm");
        assertTypeByName("text/html", "x.html");
        assertTypeByName("application/xhtml+xml", "x.xhtml");
        assertTypeByName("application/xml", "x.xml");
        assertTypeByName("application/msword", "x.doc");
        assertTypeByName("application/vnd.ms-powerpoint", "x.ppt");
        assertTypeByName("application/vnd.ms-excel", "x.xls");
        assertTypeByName("application/zip", "x.zip");
        assertTypeByName("application/vnd.oasis.opendocument.text", "x.odt");
        assertTypeByName("application/octet-stream", "x.xyz");
    }

    public void testJpegDetection() throws Exception {
        assertType("image/jpeg", "testJPEG.jpg");
        assertTypeByData("image/jpeg", "testJPEG.jpg");
        assertTypeByName("image/jpeg", "x.jpg");
        assertTypeByName("image/jpeg", "x.JPG");
        assertTypeByName("image/jpeg", "x.jpeg");
        assertTypeByName("image/jpeg", "x.JPEG");
        assertTypeByName("image/jpeg", "x.jpe");
        assertTypeByName("image/jpeg", "x.jif");
        assertTypeByName("image/jpeg", "x.jfif");
        assertTypeByName("image/jpeg", "x.jfi");
    }

    public void testTiffDetection() throws Exception {
        assertType("image/tiff", "testTIFF.tif");
        assertTypeByData("image/tiff", "testTIFF.tif");
        assertTypeByName("image/tiff", "x.tiff");
        assertTypeByName("image/tiff", "x.tif");
        assertTypeByName("image/tiff", "x.TIF");
    }

    public void testGifDetection() throws Exception {
        assertType("image/gif", "testGIF.gif");
        assertTypeByData("image/gif", "testGIF.gif");
        assertTypeByName("image/gif", "x.gif");
        assertTypeByName("image/gif", "x.GIF");
    }

    public void testPngDetection() throws Exception {
        assertType("image/png", "testPNG.png");
        assertTypeByData("image/png", "testPNG.png");
        assertTypeByName("image/png", "x.png");
        assertTypeByName("image/png", "x.PNG");
    }

    public void testBmpDetection() throws Exception {
        assertType("image/x-ms-bmp", "testBMP.bmp");
        assertTypeByData("image/x-ms-bmp", "testBMP.bmp");
        assertTypeByName("image/x-ms-bmp", "x.bmp");
        assertTypeByName("image/x-ms-bmp", "x.BMP");
        assertTypeByName("image/x-ms-bmp", "x.dib");
        assertTypeByName("image/x-ms-bmp", "x.DIB");
    }

    public void testPnmDetection() throws Exception {
        assertType("image/x-portable-bitmap", "testPBM.pbm");
        assertType("image/x-portable-graymap", "testPGM.pgm");
        assertType("image/x-portable-pixmap", "testPPM.ppm");
        assertTypeByData("image/x-portable-bitmap", "testPBM.pbm");
        assertTypeByData("image/x-portable-graymap", "testPGM.pgm");
        assertTypeByData("image/x-portable-pixmap", "testPPM.ppm");
        assertTypeByName("image/x-portable-anymap", "x.pnm");
        assertTypeByName("image/x-portable-anymap", "x.PNM");
        assertTypeByName("image/x-portable-bitmap", "x.pbm");
        assertTypeByName("image/x-portable-bitmap", "x.PBM");
        assertTypeByName("image/x-portable-graymap", "x.pgm");
        assertTypeByName("image/x-portable-graymap", "x.PGM");
        assertTypeByName("image/x-portable-pixmap", "x.ppm");
        assertTypeByName("image/x-portable-pixmap", "x.PPM");
    }

    public void testCgmDetection() throws Exception {
        // TODO: Need a test image file
        assertTypeByName("image/cgm", "x.cgm");
        assertTypeByName("image/cgm", "x.CGM");
    }

    public void testSvgDetection() throws Exception {
        assertType("image/svg+xml", "testSVG.svg");
        assertTypeByData("image/svg+xml", "testSVG.svg");
        assertTypeByName("image/svg+xml", "x.svg");
        assertTypeByName("image/svg+xml", "x.SVG");

        assertType("application/x-gzip", "testSVG.svgz");
        assertTypeByData("application/x-gzip", "testSVG.svgz");
        assertTypeByName("application/x-gzip", "x.svgz");
        assertTypeByName("application/x-gzip", "x.SVGZ");
    }

    public void testPdfDetection() throws Exception {
        assertType("application/pdf", "testPDF.pdf");
        assertTypeByData("application/pdf", "testPDF.pdf");
        assertTypeByName("application/pdf", "x.pdf");
        assertTypeByName("application/pdf", "x.PDF");
    }

    public void testSwfDetection() throws Exception {
        // TODO: Need a test flash file
        assertTypeByName("application/x-shockwave-flash", "x.swf");
        assertTypeByName("application/x-shockwave-flash", "x.SWF");
    }

    public void testWmfDetection() throws Exception {
        // TODO: Need a test wmf file
        assertTypeByName("image/x-tika-wmf", "x.wmf");
        assertTypeByName("image/x-tika-wmf", "x.WMF");
        // TODO: Need a test emf file
        assertTypeByName("image/x-tika-wmf", "x.emf");
        assertTypeByName("image/x-tika-wmf", "x.EMF");
        // TODO: Need a test wmz file
        assertTypeByName("application/x-gzip", "x.wmz");
        assertTypeByName("application/x-gzip", "x.WMZ");
        // TODO: Need a test emf file
        assertTypeByName("application/x-gzip", "x.emz");
        assertTypeByName("application/x-gzip", "x.EMZ");
    }

    public void testPsDetection() throws Exception {
        // TODO: Need a test postscript file
        assertTypeByName("application/postscript", "x.ps");
        assertTypeByName("application/postscript", "x.PS");
        assertTypeByName("application/postscript", "x.eps");
        assertTypeByName("application/postscript", "x.epsf");
        assertTypeByName("application/postscript", "x.epsi");
    }

    /**
     * @since TIKA-194
     */
    public void testJavaRegex() throws Exception{
        MimeType testType = new MimeType(this.repo, "foo/bar");
        this.repo.add(testType);
        assertNotNull(repo.forName("foo/bar"));
        String pattern = "rtg_sst_grb_0\\.5\\.\\d{8}";
        this.repo.addPattern(testType, pattern, true);
        String testFileName = "rtg_sst_grb_0.5.12345678";
        assertNotNull(this.repo.getMimeType(testFileName));
        assertEquals(this.repo.getMimeType(testFileName).getName(), "foo/bar");    
        
        MimeType testType2 = new MimeType(this.repo, "foo/bar2");
        this.repo.add(testType2);
        assertNotNull(repo.forName("foo/bar2"));
        this.repo.addPattern(testType2, pattern, false);
        assertNotNull(this.repo.getMimeType(testFileName));
        assertNotSame("foo/bar2", this.repo.getMimeType(testFileName).getName());
    }
    
    public void testRawDetection() throws Exception {
        assertTypeByName("image/x-tika-dng", "x.dng");
        assertTypeByName("image/x-tika-dng", "x.DNG");
        assertTypeByName("image/x-tika-hasselblad", "x.3fr");
        assertTypeByName("image/x-tika-fuji", "x.raf");
        assertTypeByName("image/x-tika-canon", "x.crw");
        assertTypeByName("image/x-tika-canon", "x.cr2");
        assertTypeByName("image/x-tika-kodak", "x.k25");
        assertTypeByName("image/x-tika-kodak", "x.kdc");
        assertTypeByName("image/x-tika-kodak", "x.dcs");
        assertTypeByName("image/x-tika-kodak", "x.drf");
        assertTypeByName("image/x-tika-minolta", "x.mrw");
        assertTypeByName("image/x-tika-nikon", "x.nef");
        assertTypeByName("image/x-tika-nikon", "x.nrw");
        assertTypeByName("image/x-tika-olympus", "x.orf");
        assertTypeByName("image/x-tika-pentax", "x.ptx");
        assertTypeByName("image/x-tika-pentax", "x.pef");
        assertTypeByName("image/x-tika-sony", "x.arw");
        assertTypeByName("image/x-tika-sony", "x.srf");
        assertTypeByName("image/x-tika-sony", "x.sr2");
        assertTypeByName("image/x-tika-sigma", "x.x3f");
        assertTypeByName("image/x-tika-epson", "x.erf");
        assertTypeByName("image/x-tika-mamiya", "x.mef");
        assertTypeByName("image/x-tika-leaf", "x.mos");
        assertTypeByName("image/x-tika-panasonic", "x.raw");
        assertTypeByName("image/x-tika-panasonic", "x.rw2");
        assertTypeByName("image/x-tika-phaseone", "x.cap");
        assertTypeByName("image/x-tika-phaseone", "x.iiq");
        assertTypeByName("image/x-tika-phaseone", "x.cap");
        assertTypeByName("image/x-tika-red", "x.r3d");
        assertTypeByName("image/x-tika-imacon", "x.fff");
        assertTypeByName("image/x-tika-logitech", "x.pxn");
        assertTypeByName("image/x-tika-casio", "x.bay");
        assertTypeByName("image/x-tika-rawzor", "x.rwz");
    }

    /**
     * Tests MimeTypes.getMimeType(URL), which examines both the byte header
     * and, if necessary, the URL's extension.
     */
    public void testMimeDeterminationForTestDocuments() throws Exception {
        assertType("text/html", "testHTML.html");
        assertType("application/zip", "test-documents.zip");
        // TODO: Currently returns generic MS Office type based on
        // the magic header. The getMimeType method should understand
        // MS Office types better.
        // assertEquals("application/vnd.ms-excel",
        // getMimeType("testEXCEL.xls"));
        // assertEquals("application/vnd.ms-powerpoint",
        // getMimeType("testPPT.ppt"));
        // assertEquals("application/msword", getMimeType("testWORD.doc"));
        assertType("text/html", "testHTML_utf8.html");
        assertType(
                "application/vnd.oasis.opendocument.text",
                "testOpenOffice2.odt");
        assertType("application/pdf", "testPDF.pdf");
        assertType("application/rtf", "testRTF.rtf");
        assertType("text/plain", "testTXT.txt");
        assertType("application/xml", "testXML.xml");
        assertType("audio/basic", "testAU.au");
        assertType("audio/x-aiff", "testAIFF.aif");
        assertType("audio/x-wav", "testWAV.wav");
        assertType("audio/midi", "testMID.mid");
    }

    private void assertType(String expected, String filename) throws Exception {
        InputStream stream = TestMimeTypes.class.getResourceAsStream(
                "/test-documents/" + filename);
        try {
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, filename);
            assertEquals(expected, repo.detect(stream, metadata).toString());
        } finally {
            stream.close();
        }
    }

    private void assertTypeByName(String expected, String filename)
            throws IOException {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, filename);
        assertEquals(expected, repo.detect(null, metadata).toString());
    }

    private void assertTypeByData(String expected, String filename)
            throws IOException {
        InputStream stream = TestMimeTypes.class.getResourceAsStream(
                "/test-documents/" + filename);
        try {
            Metadata metadata = new Metadata();
            assertEquals(expected, repo.detect(stream, metadata).toString());
        } finally {
            stream.close();
        }
    }

}