/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer.model

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;

class ModelIconCube {
  
  protected var hideFace:Array[Boolean] = new Array[Boolean](6);
  
  var lightValue:Int = 15;
  var useBrightness:Boolean = true;
  
  //draws a cube with these icons
  def drawCubeFromIcons(icons:Array[IIcon], tess:Tessellator, 
      minX:Float, minY:Float, minZ:Float, maxX:Float, maxY:Float, maxZ:Float, 
      minXI:Float, minYI:Float, minZI:Float, maxXI:Float, maxYI:Float, maxZI:Float){    
    
    var icon:IIcon=null;
    var minU:Float=0;
    var maxU:Float=0;
    var minV:Float=0;
    var maxV:Float=0;
    
    var v1:Vec3=null;
    var v2:Vec3=null;
    var v3:Vec3=null;
    
    var v4:Vec3 = null;
    var v5:Vec3 = null;
    var v6:Vec3 = null;
    
    if(!hideFace(0)){
      //Bottom
      icon = icons(0);
      minU = icon.getInterpolatedU(minXI*16d);
      maxU = icon.getInterpolatedU(maxXI*16d);
      minV = icon.getInterpolatedV(minZI*16d);
      maxV = icon.getInterpolatedV(maxZI*16d);
      v1 = Vec3.createVectorHelper(minX, minY, maxZ);
      v2 = Vec3.createVectorHelper(minX, minY, minZ);
      v3 = Vec3.createVectorHelper(maxX, minY, minZ);
      
      v4 = v2.subtract(v1);
      v5 = v2.subtract(v3);
      v6 = v5.crossProduct(v4).normalize();
      tess.startDrawingQuads();
      if(useBrightness)
        tess.setBrightness(lightValue);
      tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tess.setNormal(v6.xCoord.asInstanceOf[Float], v6.yCoord.asInstanceOf[Float], v6.zCoord.asInstanceOf[Float]);
      tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
      tess.addVertexWithUV(minX, minY, minZ, minU, minV);
      tess.addVertexWithUV(maxX, minY, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
      tess.draw();
    }
    if(!hideFace(1)){
      //Top
      icon = icons(1);
      minU = icon.getInterpolatedU(minXI*16d);
      maxU = icon.getInterpolatedU(maxXI*16d);
      minV = icon.getInterpolatedV(minZI*16d);
      maxV = icon.getInterpolatedV(maxZI*16d);
      
      v1 = Vec3.createVectorHelper(maxX, maxY, maxZ);
      v2 = Vec3.createVectorHelper(maxX, maxY, minZ);
      v3 = Vec3.createVectorHelper(minX, maxY, minZ);
      
      v4 = v2.subtract(v1);
      v5 = v2.subtract(v3);
      v6 = v5.crossProduct(v4).normalize();
      
      tess.startDrawingQuads();
      if(useBrightness)
        tess.setBrightness(lightValue);
      tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tess.setNormal(v6.xCoord.asInstanceOf[Float], v6.yCoord.asInstanceOf[Float], v6.zCoord.asInstanceOf[Float]);
      tess.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
      tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
      tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
      tess.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
      tess.draw();
    }
    if(!hideFace(2)){
      //North
      icon = icons(2);
      minU = icon.getInterpolatedU(minXI*16d);
      maxU = icon.getInterpolatedU(maxXI*16d);
      minV = icon.getInterpolatedV(16 - maxYI*16d);
      maxV = icon.getInterpolatedV(16 - minYI*16d);
      
      v1 = Vec3.createVectorHelper(minX, maxY, minZ);
      v2 = Vec3.createVectorHelper(maxX, maxY, minZ);
      v3 = Vec3.createVectorHelper(minX, minY, minZ);
      
      v4 = v2.subtract(v1);
      v5 = v2.subtract(v3);
      v6 = v5.crossProduct(v4).normalize();
      
      tess.startDrawingQuads();
      if(useBrightness)
        tess.setBrightness(lightValue);
      tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tess.setNormal(v6.xCoord.asInstanceOf[Float], v6.yCoord.asInstanceOf[Float], v6.zCoord.asInstanceOf[Float]);
      tess.addVertexWithUV(minX, maxY, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, maxY, minZ, minU, minV);
      tess.addVertexWithUV(maxX, minY, minZ, minU, maxV);
      tess.addVertexWithUV(minX, minY, minZ, maxU, maxV);
      tess.draw();
    }
    if(!hideFace(3)){
      //South
      icon = icons(3);
      minU = icon.getInterpolatedU(minXI*16d);
      maxU = icon.getInterpolatedU(maxXI*16d);
      minV = icon.getInterpolatedV(16 - maxYI*16d);
      maxV = icon.getInterpolatedV(16 - minYI*16d);
      
      v1 = Vec3.createVectorHelper(minX, maxY, maxZ);
      v2 = Vec3.createVectorHelper(minX, minY, maxZ);
      v3 = Vec3.createVectorHelper(maxX, minY, maxZ);
      
      v4 = v2.subtract(v1);
      v5 = v2.subtract(v3);
      v6 = v5.crossProduct(v4).normalize();
      
      tess.startDrawingQuads();
      if(useBrightness)
        tess.setBrightness(lightValue);
      tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tess.setNormal(v6.xCoord.asInstanceOf[Float], v6.yCoord.asInstanceOf[Float], v6.zCoord.asInstanceOf[Float]);
      tess.addVertexWithUV(minX, maxY, maxZ, minU, minV);
      tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
      tess.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
      tess.draw();
    }
    if(!hideFace(4)){
      //West
      icon = icons(4);
      minU = icon.getInterpolatedU(minZI*16d);
      maxU = icon.getInterpolatedU(maxZI*16d);
      minV = icon.getInterpolatedV(16 - maxYI*16d);
      maxV = icon.getInterpolatedV(16 - minYI*16d);
      
      v1 = Vec3.createVectorHelper(minX, maxY, maxZ);
      v2 = Vec3.createVectorHelper(minX, maxY, minZ);
      v3 = Vec3.createVectorHelper(minX, minY, minZ);
      
      v4 = v2.subtract(v1);
      v5 = v2.subtract(v3);
      v6 = v5.crossProduct(v4).normalize();
      
      tess.startDrawingQuads();
      if(useBrightness)
        tess.setBrightness(lightValue);
      tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tess.setNormal(v6.xCoord.asInstanceOf[Float], v6.yCoord.asInstanceOf[Float], v6.zCoord.asInstanceOf[Float]);
      tess.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
      tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
      tess.addVertexWithUV(minX, minY, minZ, minU, maxV);
      tess.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
      tess.draw();
    }
    if(!hideFace(5)){
      //East
      icon = icons(5);
      minU = icon.getInterpolatedU(minZI*16d);
      maxU = icon.getInterpolatedU(maxZI*16d);
      minV = icon.getInterpolatedV(16 - maxYI*16d);
      maxV = icon.getInterpolatedV(16 - minYI*16d);
      
      v1 = Vec3.createVectorHelper(maxX, minY, maxZ);
      v2 = Vec3.createVectorHelper(maxX, minY, minZ);
      v3 = Vec3.createVectorHelper(maxX, maxY, minZ);
      
      v4 = v2.subtract(v1);
      v5 = v2.subtract(v3);
      v6 = v5.crossProduct(v4).normalize();
      
      tess.startDrawingQuads();
      if(useBrightness)
        tess.setBrightness(lightValue);
      tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      tess.setNormal(v6.xCoord.asInstanceOf[Float], v6.yCoord.asInstanceOf[Float], v6.zCoord.asInstanceOf[Float]);
      tess.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
      tess.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
      tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
      tess.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
      tess.draw();
    }
  }

}