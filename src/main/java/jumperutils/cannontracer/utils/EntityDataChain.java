package jumperutils.cannontracer.utils;

public class EntityDataChain {
	public final long timeOfCreation;
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

	public boolean isInRange(SimpleLocation referencePos, int distance) {
		if (distance < 0) {
			return true;
		}

		for (EntityDataChainLink chainLink = start; chainLink != null; chainLink = chainLink.nextLink) {
			if (Math.abs(SimpleLocation.sub(chainLink.location, referencePos).magnitude()) <= distance) {
				return true;
			}
		}

		return false;
	}
}
