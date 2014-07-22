package com.makeso.me.droidsvg;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGElement;

public class Rasterizer {

	
	public static void main(String[] args){
		
		DPI[] generate = new DPI[]{DPI.LDPI, DPI.MDPI, DPI.HDPI, DPI.XHDPI, DPI.XXHDPI, DPI.XXXHDPI};
		
		try {
			converDir("./img", "./res", generate, DPI.MDPI);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void converDir(String inputDirPath, String outputDirPath, DPI[] useDPIs, DPI baseDPI) throws FileNotFoundException{
		File inputDir = new File(inputDirPath);
		File outputDir = new File(outputDirPath);
		outputDir.mkdirs();
		if(outputDir == null || !outputDir.isDirectory()){
			throw new FileNotFoundException(outputDirPath+"PNG doesn't exit or is not a dir");
		}
		if(inputDir == null || !inputDir.isDirectory()){
			throw new FileNotFoundException(inputDirPath+"SVG dir doesn't exit or is not a dir");
		}
		File[] inputFiles = inputDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return(file.getName().contains(".svg"));
			}
		});
		for(DPI dpi: useDPIs){
			File outDir = new File(outputDir,"drawable-"+dpi.toString().toLowerCase());
			outDir.mkdir();
			for(File file : inputFiles){
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(new File(outDir, file.getName().replace(".svg", ".png")));
					convert(file, outputStream, dpi.compareScaleTo(baseDPI));
					System.out.println("Converted"+file.getName()+"to PNG in"+dpi.toString());
				} catch (Exception e) {
					System.out.println("Failed to converted"+file.getName()+"to PNG");
					e.printStackTrace();
				}
				finally{
					if(outputStream!=null)
						try {
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}

	}
	
	public static void convert(File input, FileOutputStream output, float widthScale) throws TranscoderException, IOException{
		float width = getSVGWidth(input);
		PNGTranscoder transcoder = new PNGTranscoder();
		transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float)Math.round(width*widthScale));
		TranscoderInput inputTrans = new TranscoderInput(input.toURI().toString());
		TranscoderOutput outputTrans = new TranscoderOutput(output);
		transcoder.transcode(inputTrans, outputTrans);
		output.flush();
	}
	
	public static float getSVGWidth(File input) throws IOException{
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		SVGDocument indoc = (SVGDocument)f.createDocument(input.toURI().toString());
		String widthAttr = indoc.getDocumentElement().getAttribute("width");
		return Float.valueOf(widthAttr.replace("px", ""));
	}
	
	
	public enum DPI{
		LDPI(120),MDPI(160),HDPI(240),XHDPI(320),XXHDPI(480),XXXHDPI(640);
		
		public int level;

		private DPI(int level) {
			this.level = level;
		}
		
		public float compareScaleTo(DPI dpi){
			return this.level/(float)dpi.level;
		}
		
		public static DPI[] getAll(){
			return new DPI[] {DPI.LDPI, DPI.MDPI, DPI.HDPI, DPI.XHDPI, DPI.XXHDPI, DPI.XXXHDPI};
		}
		
		public static DPI getDPI(int level){
			DPI[] dpis = getAll();
			for(DPI dpi:dpis){
				if(dpi.level == level)
					return dpi;
			}
			return null;
		}
	}
	
	
}
