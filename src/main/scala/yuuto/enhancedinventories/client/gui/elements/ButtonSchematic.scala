/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.gui.elements

import java.util.List
import cofh.lib.gui.element.ElementButton
import cofh.lib.gui.element.ElementButtonBase
import cofh.lib.gui.GuiBase
import cofh.lib.util.helpers.StringHelper

class ButtonSchematic(gui:GuiBase, pX:Int, pY:Int, sX:Int, sY:Int, val button:Int) extends ElementButtonBase(gui, pX, pY, sX, sY){
  if(button==0){
    this.setName("buttonCancel"); 
  }else if(button==1){
    this.setName("buttonSave");
  }
  
  
  override def drawBackground(mouseX:Int, mouseY:Int, gameTicks:Float) {
    val hover=if(isOver(mouseX, mouseY)) 1 else 0;
    button match {
      case 0=>{
        gui.drawButton("IconCancel", this.posX, this.posY, 1, hover);
      }case 1=>{
        if(this.isEnabled())
          gui.drawButton("IconAccept", this.posX, this.posY, 1, hover);
        else {
          gui.drawButton("IconAcceptInactive", this.posX, this.posY, 1, 2);
        }
      }case i=>{}
    }
  }
  
  override def drawForeground(mouseX:Int, mouseY:Int) {

  }
  
  def isOver(mouseX:Int, mouseY:Int):Boolean={
    return (this.posX <= mouseX && mouseX < this.posX+this.sizeX && this.posY <= mouseY && mouseY < this.posY+this.sizeY);
  }
  
  override def onMousePressed(mouseX:Int, mouseY:Int, mouseButton:Int):Boolean={
    if(isEnabled()){
      gui.handleElementButtonClick(getName(), mouseButton);
      return true;
    }
    return false;
  }
  
  override def addTooltip(list:List[String]) {
    if(!this.isEnabled())
      return;
    button match{
      case 0=>list.add(StringHelper.localize("info.ei.clear"));
      case 1=>list.add(StringHelper.localize("info.ei.save"));
      case i=>{}
    }
  }
}