/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.gui.elements

import java.util.List;
import cofh.lib.gui.element.TabBase
import yuuto.yuutolib.tile.IRedstoneControl
import cofh.lib.gui.GuiBase
import cofh.core.gui.element.{TabRedstone => CofhTabRedstone}
import org.lwjgl.opengl.GL11
import cofh.lib.util.helpers.StringHelper

class TabRedstone(gui:GuiBase, side:Int, val myContainer:IRedstoneControl) extends TabBase(gui, side){
  this.headerColor=CofhTabRedstone.defaultHeaderColor;
  this.subheaderColor=CofhTabRedstone.defaultSubHeaderColor;
  this.textColor=CofhTabRedstone.defaultTextColor;
  this.backgroundColor=CofhTabRedstone.defaultBackgroundColor;
  this.maxHeight=112;
  this.maxWidth=112;

  def this(gui:GuiBase, container:IRedstoneControl)=this(gui, 0, container);

  override protected def drawBackground() {
    super.drawBackground();
    if(this.isFullyOpened()) {
      val var1:Float = (this.backgroundColor >> 16 & 255).toFloat / 255.0F * 0.6F;
      val var2:Float = (this.backgroundColor >> 8 & 255).toFloat / 255.0F * 0.6F;
      val var3:Float = (this.backgroundColor & 255).toFloat / 255.0F * 0.6F;
      GL11.glColor4f(var1, var2, var3, 1.0F);
      this.gui.drawTexturedModalRect(this.posX() + 24, this.posY + 16, 16, 20, 64, 44);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
  }
  override def drawForeground(){

    drawBackground();
    drawTabIcon("IconRedstone");
    if (!isFullyOpened()) {
      return;
    }
    getFontRenderer().drawStringWithShadow(StringHelper.localize("info.cofh.redstoneControl"), posXOffset() + 18, posY + 6, headerColor);
    getFontRenderer().drawStringWithShadow(StringHelper.localize("info.cofh.controlStatus") + ":", posXOffset() + 6, posY + 62, subheaderColor);
    getFontRenderer().drawStringWithShadow(StringHelper.localize("info.ei.active") + ":", posXOffset() + 6, posY + 86, subheaderColor);
    //getFontRenderer().drawStringWithShadow(StringHelper.localize("info.cofh.controlStatus") + ":", posXOffset() + 6, posY + 42, subheaderColor);
    //getFontRenderer().drawStringWithShadow(StringHelper.localize("info.cofh.signalRequired") + ":", posXOffset() + 6, posY + 66, subheaderColor);

    if (myContainer.getControl().isAlways()) {
      gui.drawButton("IconGunpowder", posX() + 28, posY + 20, 1, 1);
      gui.drawButton("IconRSTorchOff", posX() + 48, posY + 20, 1, 0);
      gui.drawButton("IconRSTorchOn", posX() + 68, posY + 20, 1, 0);
      gui.drawButton("IconRepeater", posX()+28, posY+40, 1, 0);
      gui.drawButton("IconNever", posX()+48, posY+40, 1, 0);
      getFontRenderer().drawString(StringHelper.localize("info.cofh.disabled"), posXOffset() + 14, posY + 74, textColor);
      getFontRenderer().drawString(StringHelper.localize("info.ei.always"), posXOffset() + 14, posY + 98, textColor);
      //getFontRenderer().drawString(StringHelper.localize("info.cofh.disabled"), posXOffset() + 14, posY + 54, textColor);
      //getFontRenderer().drawString(StringHelper.localize("info.cofh.ignored"), posXOffset() + 14, posY + 78, textColor);
    } else if(myContainer.getControl().isNever()){
      gui.drawButton("IconRedstone", posX() + 28, posY + 20, 1, 0);
      gui.drawButton("IconRSTorchOff", posX() + 48, posY + 20, 1, 0);
      gui.drawButton("IconRSTorchOn", posX() + 68, posY + 20, 1, 0);
      gui.drawButton("IconRepeater", posX()+28, posY+40, 1, 0);
      gui.drawButton("IconNever", posX()+48, posY+40, 1, 1);
      getFontRenderer().drawString(StringHelper.localize("info.cofh.disabled"), posXOffset() + 14, posY + 74, textColor);
      getFontRenderer().drawString(StringHelper.localize("info.ei.never"), posXOffset() + 14, posY + 98, textColor);
    }else{
      getFontRenderer().drawString(StringHelper.localize("info.cofh.enabled"), posXOffset() + 14, posY + 74, textColor);
      //getFontRenderer().drawString(StringHelper.localize("info.cofh.enabled"), posXOffset() + 14, posY + 54, textColor);

      if (myContainer.getControl().isLow()) {
        gui.drawButton("IconRedstone", posX() + 28, posY + 20, 1, 0);
        gui.drawButton("IconRSTorchOff", posX() + 48, posY + 20, 1, 1);
        gui.drawButton("IconRSTorchOn", posX() + 68, posY + 20, 1, 0);
        gui.drawButton("IconRepeater", posX()+28, posY+40, 1, 0);
        gui.drawButton("IconNever", posX()+48, posY+40, 1, 0);
        getFontRenderer().drawString(StringHelper.localize("info.ei.low"), posXOffset() + 14, posY + 98, textColor);
        //getFontRenderer().drawString(StringHelper.localize("info.cofh.low"), posXOffset() + 14, posY + 78, textColor);
      } else if(myContainer.getControl().isHigh()){
        gui.drawButton("IconRedstone", posX() + 28, posY + 20, 1, 0);
        gui.drawButton("IconRSTorchOff", posX() + 48, posY + 20, 1, 0);
        gui.drawButton("IconRSTorchOn", posX() + 68, posY + 20, 1, 1);
        gui.drawButton("IconRepeater", posX()+28, posY+40, 1, 0);
        gui.drawButton("IconNever", posX()+48, posY+40, 1, 0);
        getFontRenderer().drawString(StringHelper.localize("info.ei.high"), posXOffset() + 14, posY + 98, textColor);
        //getFontRenderer().drawString(StringHelper.localize("info.cofh.high"), posXOffset() + 14, posY + 78, textColor);
      }else{
        gui.drawButton("IconRedstone", posX() + 28, posY + 20, 1, 0);
        gui.drawButton("IconRSTorchOff", posX() + 48, posY + 20, 1, 0);
        gui.drawButton("IconRSTorchOn", posX() + 68, posY + 20, 1, 0);
        gui.drawButton("IconRepeater", posX()+28, posY+40, 1, 1);
        gui.drawButton("IconNever", posX()+48, posY+40, 1, 0);
        getFontRenderer().drawString(StringHelper.localize("info.ei.pulse"), posXOffset() + 14, posY + 98, textColor);
      }
    }
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }

  override def addTooltip(list:List[String]) {

    if (!isFullyOpened()) {
      if (myContainer.getControl().isAlways()) {
        list.add(StringHelper.localize("info.cofh.disabled") + ", " + StringHelper.localize("info.ei.always"));
        return;
      } else if (myContainer.getControl().isLow()) {
        list.add(StringHelper.localize("info.cofh.enabled") + ", " + StringHelper.localize("info.ei.low"));
        return;
      } else if (myContainer.getControl().isHigh()){
        list.add(StringHelper.localize("info.cofh.enabled") + ", " + StringHelper.localize("info.ei.high"));
        return;
      } else if (myContainer.getControl().isPulsing()){
        list.add(StringHelper.localize("info.cofh.enabled") + ", " + StringHelper.localize("info.ei.pulse"));
        return;
      } else if (myContainer.getControl().isNever()){
        list.add(StringHelper.localize("info.cofh.disabled") + ", " + StringHelper.localize("info.ei.never"));
        return;
      }
      return;
    }
    var x:Int = gui.getMouseX();
    if(side == TabBase.LEFT) {
      x+=currentWidth;
    }
    x -= currentShiftY;
    val y:Int = gui.getMouseY() - currentShiftY;
    if (28 <= x && x < 44 && 20 <= y && y < 36) {
      list.add(StringHelper.localize("info.ei.always"));
    } else if (48 <= x && x < 64 && 20 <= y && y < 36) {
      list.add(StringHelper.localize("info.ei.low"));
    } else if (68 <= x && x < 84 && 20 <= y && y < 36) {
      list.add(StringHelper.localize("info.ei.high"));
    } else if (28 <= x && x < 44 && 40 <= y && y < 56){
      list.add(StringHelper.localize("info.ei.pulse"));
    } else if (48 <= x && x < 64 && 40 <= y && y < 56){
      list.add(StringHelper.localize("info.ei.never"))
    }
  }

  override def onMousePressed(mx:Int, my:Int, mb:Int):Boolean={

    if (!isFullyOpened()) {
      return false;
    }
    var mouseX=mx;
    var mouseY=my;
    if (side == TabBase.LEFT) {
      mouseX += currentWidth;
    }
    mouseX -= currentShiftX;
    mouseY -= currentShiftY;

    if (mouseX < 24 || mouseX >= 88 || mouseY < 16 || mouseY >= 56) {
      return false;
    }
    if (28 <= mouseX && mouseX < 44 && 20 <= mouseY && mouseY < 36) {
      if (!myContainer.getControl().isAlways()) {
        myContainer.setControl(IRedstoneControl.ControlMode.ALWAYS);
        GuiBase.playSound("random.click", 1.0F, 0.4F);
      }
    } else if (48 <= mouseX && mouseX < 64 && 20 <= mouseY && mouseY < 36) {
      if (!myContainer.getControl().isLow()) {
        myContainer.setControl(IRedstoneControl.ControlMode.LOW);
        GuiBase.playSound("random.click", 1.0F, 0.6F);
      }
    } else if (68 <= mouseX && mouseX < 84 && 20 <= mouseY && mouseY < 36) {
      if (!myContainer.getControl().isHigh()) {
        myContainer.setControl(IRedstoneControl.ControlMode.HIGH);
        GuiBase.playSound("random.click", 1.0F, 0.8F);
      }
    } else if (28 <= mouseX && mouseX < 44 && 40 <= mouseY && mouseY < 56) {
      if (!myContainer.getControl().isPulsing()) {
        myContainer.setControl(IRedstoneControl.ControlMode.PULSING);
        GuiBase.playSound("random.click", 1.0f, 0.8f);
      }
    } else if (48 <= mouseX && mouseX < 64 && 40 <= mouseY && mouseY < 56) {
      if (!myContainer.getControl().isNever()) {
        myContainer.setControl(IRedstoneControl.ControlMode.NEVER);
        GuiBase.playSound("random.click", 1.0f, 0.8f);
      }
    }
    return true;
  }

}
