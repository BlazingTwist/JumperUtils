package jumperutils.cannontracer.utils;

public class EntityDataChain {
	public long timeOfCreation;
	public EntityDataChainLink start;
	public EntityDataChainLink end;
	
	public EntityDataChain(EntityDataChainLink link) {
		this.timeOfCreation = System.currentTimeMillis();
		start = link;
		end = link;
	}
	
	public void addLinkAtEnd(EntityDataChainLink link) {
		link.setPreviousLink(this.end.setNextLink(link));
		this.end = link;
	}
}
