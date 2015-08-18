package yuuto.enhancedinventories.util
import org.apache.logging.log4j.Level
import cpw.mods.fml.common.FMLLog;
import yuuto.enhancedinventories.ref.ReferenceEI

object LogHelperEI {
	
	private val debug = false;
	
	def All(obj:Any){
		LogSpecial(Level.ALL, obj);
	}
	def Debug(obj:Any){
		if(debug)
			Info(obj);
		LogSpecial(Level.DEBUG, obj);
	}
	def Error(obj:Any){
		LogSpecial(Level.ERROR, obj);
	}
  def Error(obj:Any, t:Throwable){
    LogSpecial(Level.ERROR, obj, t);
  }
	def Fatal(obj:Any){
		LogSpecial(Level.FATAL, obj);
	}
	def Info(obj:Any){
		LogSpecial(Level.INFO, obj);
	}
	def Off(obj:Any){
		LogSpecial(Level.OFF, obj);
	}
	def Trace(obj:Any){
		LogSpecial(Level.TRACE, obj);
	}
	def Warning(obj:Any){
		LogSpecial(Level.WARN, obj);
	}
	def LogSpecial(logLevel:Level, obj:Any){
		FMLLog.log(ReferenceEI.MOD_NAME, logLevel, String.valueOf(obj));
	}
  def LogSpecial(logLevel:Level, obj:Any, t:Throwable){
    FMLLog.log(ReferenceEI.MOD_NAME, logLevel, t, String.valueOf(obj));
  }

}