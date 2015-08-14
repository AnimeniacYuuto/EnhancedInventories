/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json.exception

class InvalidFrameException(tpe:Int) extends Exception{
  
  def getMessage(fileName:String, key:String):String={
    return "No name for "+key+" in file "+fileName;
  }
}