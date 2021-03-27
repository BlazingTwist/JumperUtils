package jumperutils.cannontester;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jumperutils.JumperUtils;
import jumperutils.cannontester.command.CannonTesterCMD;
import jumperutils.cannontester.listeners.PluginMessageListener;
import jumperutils.cannontester.listeners.ServerCommandListener;

public class CannonTester {
	public final JumperUtils main;
	public final PluginMessageListener pluginMessageListener;
	public final ServerCommandListener serverCommandListener;
	public final CannonTesterCMD cannonTesterCMD;
	public final ObjectMapper objectMapper;

	public CannonTester(JumperUtils main) {
		this.main = main;
		pluginMessageListener = new PluginMessageListener(this);
		serverCommandListener = new ServerCommandListener(this);
		cannonTesterCMD = new CannonTesterCMD(this);
		objectMapper = new ObjectMapper()
				.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES,
						MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS,
						MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
				.disable(MapperFeature.AUTO_DETECT_CREATORS,
						MapperFeature.AUTO_DETECT_FIELDS,
						MapperFeature.AUTO_DETECT_GETTERS,
						MapperFeature.AUTO_DETECT_IS_GETTERS);
	}
}
