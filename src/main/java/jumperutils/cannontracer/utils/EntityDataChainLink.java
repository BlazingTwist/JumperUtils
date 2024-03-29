package jumperutils.cannontracer.utils;

public class EntityDataChainLink {
	public final SimpleLocation location;
	public EntityDataChainLink previousLink;
	public EntityDataChainLink nextLink;
	
	public EntityDataChainLink(double x, double y, double z) {
		this.location = new SimpleLocation(x, y, z);
		previousLink = null;
		nextLink = null;
	}
	public EntityDataChainLink(SimpleLocation location) {
		this.location = location;
		previousLink = null;
		nextLink = null;
	}
	
	public EntityDataChainLink setPreviousLink(EntityDataChainLink prev){
		previousLink = prev;
		return this;
	}
	
	public EntityDataChainLink setNextLink(EntityDataChainLink next){
		nextLink = next;
		return this;
	}
	
	public boolean isFirstLink() {
		return previousLink == null;
	}
	
	public boolean isLastLink() {
		return nextLink == null;
	}
}
