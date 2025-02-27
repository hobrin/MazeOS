package main;


import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import graphics.Shader;
import math.Matrix4f;
import math.Vector3f;

public class GameThread implements Runnable{
	
	
	public long window;
	private long monitor;
	
	private long start;
	private long accumulator = 0;
	private long end;
	

	private static final long TPT = 1000/125;

	
	

	public static final int WIDTH = 1280, HEIGHT = 720;
	
	public static final float SCREEN_RATIO = (float) HEIGHT / (float) WIDTH;
	

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		init();
		
		
		while(true){
			
			if (GLFW.glfwWindowShouldClose(window)) {
				GLFW.glfwDestroyWindow(window);
				break;
			}
			
			start = System.currentTimeMillis();
			
			
			if (accumulator > TPT){
				update();
				render();
				
				accumulator-=TPT;
			}
	
			
			end = System.currentTimeMillis();
			
			accumulator+=(end-start);
			
			
		}
		
		
		
	}
	
	private void init(){
		
		//setup error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
		{
			// TODO: Handle error
		}
		
		monitor = GLFW.glfwGetPrimaryMonitor();
		
//		window = GLFW.glfwCreateWindow(GLFW.glfwGetVideoMode(monitor).width(), GLFW.glfwGetVideoMode(monitor).height(), "MazeGame", monitor, 0);
		
		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "MazeGame", 0, 0);
		
		
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GLFW.glfwShowWindow(window);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		//enable textures
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

		//mouse motion
		if (GLFW.glfwRawMouseMotionSupported())
			GLFW.glfwSetInputMode(window, GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
		else System.out.println("Raw mouse not supported; use arrow keys instead.");
		
		
		GL11.glClearColor(0.2f, 0.4f, 0.8f, 1.0f);
		
		
		
		System.out.println("Running LWJGL Version "+ Version.getVersion()+"!");
		System.out.println("Running OpenGL Version "+GL11.glGetString(GL11.GL_VERSION)+"!");
		
		Shader.loadAll();
		
		GameStateManager.setWindow(window);
		GLFW.glfwSetKeyCallback(GameStateManager.window, Input.controls);
		GameStateManager.goTo(GameStateManager.Title);
	}
	
	private void update(){

		GLFW.glfwPollEvents();
		
		GameStateManager.update();
		
		

	}
	
	private void render(){
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GameStateManager.render();
		
		GLFW.glfwSwapBuffers(window);
		
	}

}
