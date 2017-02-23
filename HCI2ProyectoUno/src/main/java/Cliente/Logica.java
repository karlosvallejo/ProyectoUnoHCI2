package Cliente;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Modelo.PathFinder;
import Serializable.Balance;
import Serializable.BalanceCompleto;
import Serializable.Mensaje;
import pathfinder.GraphNode;
import processing.core.PApplet;

public class Logica implements Observer{
  ComunicacionCliente com;
  PApplet app;
  int estados=0;
  boolean checkInstrucciones=false;
  boolean ckeckPopUp;
  boolean checkPopUpOtroJugador;
  Mensaje mj;
  Balance bl;
  BalanceCompleto balanceCo;
  PathFinder rutas;
	GraphNode[] g;
	private GraphNode startNode;
	private int estadoRonda;
	String sugerencia;
	private boolean checkInstruccionesOtroJugador;
	
	boolean verdadOtroJugador;
	boolean dijeVerdad;
	boolean confie;
	boolean elOtroJugadorConfio;
	
	String mostrarBalance=null;
	
	public Logica(PApplet app) {
		this.app=app;
		com= new ComunicacionCliente(3010);
		com.addObserver(this);
		new Thread(comprobaciones()).start();
	}
	
	
	
	public void pintar(){
		
		switch (estados) {
		case 0:
			primeraPantalla();
			break;

		case 1:
	
			segundaPantalla();
			break;
			
			
		case 2:
			
			terceraPantalla();
			
			break;
			
			
		case 3:
			
			
			
			
			break;
				
		}
		
	}
	
	private void primeraPantalla(){
		if(com.servidor==null){
			app.fill(0);
			app.text("conectando con el otro jugador", app.width/2, app.height/2);
		} else {
			
			estados=1;
		}
	}
	
	
	private void segundaPantalla(){

		app.text("pantallaDos", 100, 100);
		if(checkInstrucciones){
			app.fill(0,200,0);
		} else{
			app.fill(0);
		}
		app.rect(app.width-60, app.height-60, 30, 30);
	}
	
	private void terceraPantalla () {
		switch (estadoRonda) {
		case 0:
			app.fill(0);
			
			app.text("Sugiere una direccion para el otro jugador", 200, 100);
			rutas.pintar();
			
			app.text("Izquierda", 600, 400);
			app.text("Derecha", 1000, 400);
			app.text("Derecho", 800, 200);
			app.text("Atras", 800, 600);
		//	g= rutas.rNodes;
			if(g!=null){
			posicionesJugador(g);
			}
			break;

		case 1:
			if(sugerencia==null){
			app.text("esperando sugerencia del otro jugador", 300, 300);
			} else {
				app.fill(0);
				
				app.text("el otro jugador sugurio:"+ sugerencia, 200, 100);
				rutas.pintar();
				
				app.text("Izquierda", 600, 400);
				app.text("Derecha", 1000, 400);
				app.text("Derecho", 800, 200);
				app.text("Atras", 800, 600);
			//	g= rutas.rNodes;
				if(g!=null){
				posicionesJugador(g);
				}
			}
			
			
			
			break;
			
			
		case 2:
             
			app.fill(0);
			
			app.text("el otro jugador sugurio:"+ sugerencia, 200, 100);
			rutas.pintar();
			
			app.text("Izquierda", 600, 400);
			app.text("Derecha", 1000, 400);
			app.text("Derecho", 800, 200);
			app.text("Atras", 800, 600);
		//	g= rutas.rNodes;
			if(g!=null){
			posicionesJugador(g);
			}
			app.fill(150);
			app.rect(400,300, 400, 400);
			app.fill(50);
			app.text("aqui va el balance de confianza", 370, 230);
			if(verdadOtroJugador){
			    app.text("El otro jugador sugurio: "+sugerencia +". dijo : verdad", 370, 250);
			} else if(!verdadOtroJugador) {
				app.text("El otro jugador sugurio: "+sugerencia +". dijo : mentira", 370, 250);
			}
			
			
			if(confie){
			    app.text("confié en el otro jugador", 370, 300);
			} else if(!confie) {
				app.text("no confié en el otro jugador", 370, 300);
			}
			
			if(dijeVerdad){
			app.text("Dije la verdad", 370, 350);
			} else {
				app.text("no dije la verdad", 370, 350);
			}
			
			if(elOtroJugadorConfio){
				app.text("el otro jugador confio", 370, 400);
				} else {
					app.text("el otro jugador no confio", 370, 400);
				}
			
			
			break;
		}
	}
	
	
	 void movimiento(int mouseX, int mouseY){
		 switch (estadoRonda) {
		case 0:
			
			app.println(mouseX,mouseY);
			if( (mouseX>751 && mouseX<850) && (mouseY>175 && mouseY<210)){
				System.out.println("derecho");
				ArrayList<String> secuencia= rutas.getSequence();
				if(secuencia.get(0).equals("arriba")){
					System.out.println("verdad para arriba");
					com.enviarObjeto(new Balance("arriba",true));
					dijeVerdad=true;
					
				} else {
					System.out.println("mentira para arriba");
					com.enviarObjeto(new Balance("arriba",false));
					dijeVerdad=false;
				}
				estadoRonda+=1;
				
			//	server.enviarObjeto(new Mensaje(startNode));
				
				
				
				
				
			
			} else if((mouseX>550 && mouseX<650) && (mouseY>380 && mouseY<415)){
				System.out.println("izquierda");
				ArrayList<String> secuencia= rutas.getSequence();
				if(secuencia.get(0).equals("izq")){
					System.out.println("verdad para izquierda");
					com.enviarObjeto(new Balance("izq",true));
					dijeVerdad=true;
					
				} else {
					System.out.println("mentira para izquierda");
					com.enviarObjeto(new Balance("izq",false));
					dijeVerdad=false;
				}
				estadoRonda+=1;
				
				
			} else if((mouseX>950 && mouseX<1050) && (mouseY>380 && mouseY<415)){
				System.out.println("derecha");
				ArrayList<String> secuencia= rutas.getSequence();
				if(secuencia.get(0).equals("der")){
					System.out.println("verdad para derecha");
					com.enviarObjeto(new Balance("der",true));
					dijeVerdad=true;
				} else {
					System.out.println("mentira para derecha");
					com.enviarObjeto(new Balance("der",false));
					dijeVerdad=false;
				}
				
				estadoRonda+=1;
				
				
			} else if ((mouseX>766 && mouseX<830) && (mouseY>570 && mouseY<608)){
				System.out.println("atras");
				ArrayList<String> secuencia= rutas.getSequence();
				if(secuencia.get(0).equals("abajo")){
					System.out.println("verdad para abajo");
					com.enviarObjeto(new Balance("abajo",true));
					dijeVerdad=true;
				} else {
					System.out.println("mentira para abajo");
					com.enviarObjeto(new Balance("abajo",false));
					dijeVerdad=false;
				}
				estadoRonda+=1;
				
			}
			
			break;

		case 1:
			app.println(mouseX,mouseY);
			if( (mouseX>751 && mouseX<850) && (mouseY>175 && mouseY<210)){
				System.out.println("derecho");
				startNode = rutas.gs.getNodeAt(g[0].xf()+rutas.offX - rutas.offX, g[0].yf()+rutas.offY-20 - rutas.offY, 0,10.0f); 
				int imprimir= (int) (g[0].yf()+rutas.offY-5) ;
				app.println("posicion apuntada:" + imprimir);
				if(startNode!=null){
				System.out.println("id del nodo: "+startNode.id());
				
				g[0]=startNode;
				
				com.enviarObjeto(new Mensaje(startNode));
				
				if(sugerencia.equals("arriba")){
					confie=true;
				} else{
					confie=false;
				}
				
			
				
				}
				//-----------------
				com.enviarObjeto(new BalanceCompleto(confie, dijeVerdad));
				//---------------
				//estadoRonda+=1;
				mostrarBalance="yes";
			
			} else if((mouseX>550 && mouseX<650) && (mouseY>380 && mouseY<415)){
				System.out.println("izquierda");
					startNode = rutas.gs.getNodeAt(g[0].xf()+rutas.offX-17 - rutas.offX, g[0].yf()+rutas.offY - rutas.offY, 0, 16.0f); 
					if(startNode!=null){
					System.out.println("id del nodo: "+startNode.id());
					
					g[0]=startNode;
					
				   com.enviarObjeto(new Mensaje(startNode));
				   
					if(sugerencia.equals("izq")){
						confie=true;
					} else{
						confie=false;
					}
					
					
					
					}
					//-----------------
					com.enviarObjeto(new BalanceCompleto(confie, dijeVerdad));
					//---------------
					//estadoRonda+=1;
					mostrarBalance="yes";
					
			} else if((mouseX>950 && mouseX<1050) && (mouseY>380 && mouseY<415)){
				System.out.println("derecha");
				startNode = rutas.gs.getNodeAt(g[0].xf()+rutas.offX+20 - rutas.offX, g[0].yf()+rutas.offY - rutas.offY, 0, 10.0f); 
				if(startNode!=null){
				System.out.println("id del nodo: "+startNode.id());
				
				g[0]=startNode;
				
				com.enviarObjeto(new Mensaje(startNode));
				
				
				if(sugerencia.equals("der")){
					confie=true;
				} else{
					confie=false;
				}
				
				
				
				}
				//-----------------
				com.enviarObjeto(new BalanceCompleto(confie, dijeVerdad));
				//---------------
				//estadoRonda+=1;
				
				mostrarBalance="yes";
				
				
			} else if ((mouseX>766 && mouseX<830) && (mouseY>570 && mouseY<608)){
				System.out.println("atras");
				startNode = rutas.gs.getNodeAt(g[0].xf()+rutas.offX - rutas.offX, g[0].yf()+rutas.offY+20 - rutas.offY, 0, 10.0f); 
				if(startNode!=null){
				System.out.println("id del nodo: "+startNode.id());
				
				g[0]=startNode;
				
				com.enviarObjeto(new Mensaje(startNode));
				
				
				if(sugerencia.equals("abajo")){
					confie=true;
				} else{
					confie=false;
				}
				
			
				
				}
				//-----------------
				com.enviarObjeto(new BalanceCompleto(confie, dijeVerdad));
				//---------------
				//estadoRonda+=1;
				
				mostrarBalance="yes";
			}
			
			
			break;
			
		case 2:
			if ((mouseX>200 && mouseX<600) && (mouseY>100 && mouseY<500)){
				System.out.println("cerrar");
				com.enviarObjeto(new Mensaje(true));
				ckeckPopUp=true;
			}
			
			
			
			
			break;
		}
		
		
		
		
	}
	
	
	
	private void posicionesJugador(GraphNode[] r){
		app.pushMatrix();
		app.translate(rutas.offX, rutas.offY);
			if (r.length >= 2) {
				app.pushStyle();
				// Route start node
				app.strokeWeight(2.0f);
				app.stroke(0, 0, 160);
				app.fill(0, 0, 255);
				app.ellipse(r[0].xf(), r[0].yf(), 10, 10);
				// Route end node
				app.stroke(0, 250, 0);
				app.fill(0, 250, 0);
				app.ellipse(r[r.length - 1].xf(), r[r.length - 1].yf(), 10, 10);
				
				app.popStyle();
			}
			app.popMatrix();
		
	}
	
	
	private Runnable comprobaciones(){
		
		Runnable r= new Runnable() {
			
			

			@Override
			public void run() {
				
				while(true){
					try {
						
						switch (estados) {
						case 1:
							if(mj!= null){
								checkInstruccionesOtroJugador= mj.checkeado;
							}
							
								if(checkInstrucciones && checkInstruccionesOtroJugador){
									rutas= new PathFinder(app,292,195);
									GraphNode[] o= rutas.rNodes;
									   com.enviarObjeto(new Mensaje(o));
								estados=2;
								}
						
							

							break;

						case 2:
							if(mj!= null){
								checkPopUpOtroJugador= mj.checkeado;
							}
							
								if(ckeckPopUp && checkPopUpOtroJugador){
									estadoRonda=0;
									ckeckPopUp=false;
									checkInstruccionesOtroJugador=false;
									sugerencia=null;
								}
								
								if(balanceCo!=null && estadoRonda==1 && mostrarBalance!=null){
									estadoRonda+=1;
								}
							
							
							break;
						}
						
						Thread.sleep(20);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
			
					
				}
				
			}
		};
		
		
		return r;
		
	}




	public void update(Observable o, Object arg) {
	Object ob=arg;
	
		if(ob instanceof Mensaje){
			mj=(Mensaje) ob;
			if(mj.nodos!=null && estados==2){
				g=mj.nodos;
			}
			
			if(mj.nodoInicial!=null && estados==2){
			//	System.out.println("entro update 2");;
				rutas.start= mj.nodoInicial.id();
				rutas.usePathFinder(rutas.pathFinder);
				
				System.out.println("--------------Pasos realizados-----------");
				System.out.println(rutas.getSequence());
			}
			
			
		} else if(ob instanceof Balance){
			bl= (Balance) ob;
			if(bl.indicacion!=null){
				sugerencia=bl.indicacion;
			}
			
			if(bl.dijoVerdad){
				verdadOtroJugador=true;
			}else{
				verdadOtroJugador=false;
			}
		} else if(ob instanceof BalanceCompleto){
			balanceCo= (BalanceCompleto) ob;
			elOtroJugadorConfio= balanceCo.confie;
			
		}
		
	}

}
