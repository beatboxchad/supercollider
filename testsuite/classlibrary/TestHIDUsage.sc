TestHIDUsage : UnitTest {
	classvar <usagePages;
	*initClass {
		usagePages = (Platform.resourceDir ++ "/HID_Support/hut/HidUsageTables.json").parseJSONFile["UsagePages"].select(
			{|page|
				((1..16) ++ [20, 64]).includes(page["Id"].asInteger) // TODO open this up to the full list of usages, including generated
			}
		);
	^super.initClass();
	}

	test_getUsageDescription_returnsPageNameAndUsageName {
		^usagePages.do({|page|
                        var expected = page["UsageIds"].collect({|uid|[page["Name"], uid["Name"]]});
                        var result = page["UsageIds"].collect({|uid|HIDUsage.getUsageDescription(page["Name"], uid["Name"])});
			page["UsageIds"].do({|usage|
				var expected = [page["Name"], usage["Name"]];
				var result = HIDUsage.getUsageDescription(page["Id"].asInteger, usage["Id"].asInteger);
				this.assertEquals(result, expected,
					"HIDUsage.getUsageDescription(%, %) should return %; got %".format(
						page["Id"], usage["Id"], expected, result
					));
			});
		});
	}
	test_idsToName_GetsUsageName {
		^usagePages.do({|page|
			page["UsageIds"].do({|usage|
				var expected = usage["Name"];
				var result = HIDUsage.idsToName(page["Id"].asInteger, usage["Id"].asInteger);
				this.assertEquals(
					result,
					expected,
					"HIDUsage.idsToName(%, %) should return usage name %; got %".format(
						page["Id"], usage["Id"], expected, result
					);
				);
			});
		});
	}
	test_getUsageIds_GetsUsage_Ids {
		^usagePages.do({|page|
			page["UsageIds"].do({|usage|
				var usageName = usage["Name"];
				var expected = [page["Id"], usage["Id"]];
				var result = HIDUsage.getUsageIds(usageName);
				this.assertEquals(
					result,
					expected,
					"HIDUsage.getUsageIds(%) should return \"%\"; got \"%\"".format(
						usageName, expected, result
					)
				)
			});
		})
	}
}
