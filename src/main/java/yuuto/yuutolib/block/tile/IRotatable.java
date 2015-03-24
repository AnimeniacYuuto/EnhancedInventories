package yuuto.yuutolib.block.tile;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRotatable {
	/**
	 * Gets the orientation of the machine
	 * @return the orientation of the machine
	 */
	public ForgeDirection getOrientation();
	/**
	 * sets the orientation of the machine
	 * @param dir
	 * @return the new orientation of the machine
	 */
	public ForgeDirection setOrientation(ForgeDirection dir);
	/**
	 * Rotates the machine around the given axis
	 * @param axis
	 * @return the new orientation of the machine
	 */
	public ForgeDirection rotateAround(ForgeDirection axis);
	/**
	 * is the machine doing work?
	 * @return weather or not the machine is doing work
	 */
}
