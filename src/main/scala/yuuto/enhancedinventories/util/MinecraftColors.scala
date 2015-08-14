package yuuto.enhancedinventories.util

import java.awt.Color

/**
 * @author Jacob
 */
object MinecraftColors{
  private val WoolColors:Array[MinecraftColors]=new Array[MinecraftColors](16);
  private val DyeColors:Array[MinecraftColors]=new Array[MinecraftColors](16);
  
  val WHITE=new MinecraftColors(0,"color.White",221,221,221);
  val ORANGE=new MinecraftColors(1, "color.Orange", 219, 125, 62);
  val MAGENTA=new MinecraftColors(2, "color.Magenta", 179, 80, 188);
  val LIGHT_BLUE=new MinecraftColors(3, "color.LightBlue", 107, 138, 201);
  val YELLOW=new MinecraftColors(4, "color.Yellow", 177, 166, 39);
  val LIME=new MinecraftColors(5, "color.Lime", 65, 174, 56);
  val PINK=new MinecraftColors(6, "color.Pink", 208, 132, 153);
  val GRAY=new MinecraftColors(7, "color.Gray", 64, 64, 64);
  val LIGHT_GRAY=new MinecraftColors(8, "color.LightGray", 134, 141, 141);
  val CYAN=new MinecraftColors(9, "color.Cyan", 46, 110, 137);
  val PURPLE=new MinecraftColors(10, "color.Purple", 126, 61, 181);
  val BLUE=new MinecraftColors(11, "color.Blue", 46, 56, 141);
  val BROWN=new MinecraftColors(12, "color.Brown", 79, 50, 31);
  val GREEN=new MinecraftColors(13, "color.Green", 53, 70, 27);
  val RED=new MinecraftColors(14, "color.Red", 150,52,48);
  val BLACK=new MinecraftColors(15, "color.Black", 22,22,22);
  
  def dye(index:Int):MinecraftColors={
    return DyeColors(index);
  }
  def getDyeColors():Array[MinecraftColors]={
    return DyeColors.clone();
  }
  def wool(index:Int):MinecraftColors={
    return WoolColors(index);
  }
  def getWoolColors():Array[MinecraftColors]={
    return WoolColors.clone();
  }
}
class MinecraftColors private(private val index:Int, private val name:String, private val color:Color) {
  MinecraftColors.WoolColors(index)=this;
  MinecraftColors.DyeColors(15-index)=this;
  
  private def this(index:Int, name:String, r:Int, g:Int, b:Int)=this(index, name, new Color(r,g,b));
  private def this(index:Int, name:String, r:Int, g:Int, b:Int, a:Int)=this(index, name, new Color(r,g,b,a));
  private def this(index:Int, name:String, r:Float, g:Float, b:Float)=this(index, name, new Color(r,g,b));
  private def this(index:Int, name:String, r:Float, g:Float, b:Float, a:Float)=this(index, name, new Color(r,g,b,a));
  
  def getColor():Color={
    return color;
  }
  def getUnlocalizedName():String={
    return name;
  }
  def getIndex():Int={
    return index;
  }
  
}