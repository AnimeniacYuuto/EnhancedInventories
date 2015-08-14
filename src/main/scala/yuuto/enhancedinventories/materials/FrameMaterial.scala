/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.materials

import java.awt.Color;

class FrameMaterial(id:String, textureIndex:Int, color:Color) {
  
  def this(id:String, textureIndex:Int, r:Int, g:Int, b:Int)=this(id, textureIndex, new Color(r,g,b));
  def this(id:String, textureIndex:Int, r:Int, g:Int, b:Int, a:Int)=this(id, textureIndex, new Color(r,g,b,a));
  
  def this(id:String, textureIndex:Int, r:Float, g:Float, b:Float)=this(id, textureIndex, new Color(r,g,b));
  def this(id:String, textureIndex:Int, r:Float, g:Float, b:Float, a:Float)=this(id, textureIndex, new Color(r,g,b,a));
  
  def getID():String=id;
  def getTextureIndex():Int=textureIndex;
  def color():Color=color;

}