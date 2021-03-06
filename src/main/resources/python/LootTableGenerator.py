import os
# Generates a basic loot table entry for every essentials blocks with a defined blockstate file,
# where the blocks drops itself

blockstates = os.listdir("../assets/crossroads/blockstates/")

regNames = [os.path.basename(bstate) for bstate in blockstates]

loottablePath = "../data/crossroads/loot_tables/blocks"

for prevTable in os.listdir(loottablePath):
	if os.path.isfile(prevTable):
		os.unlink(prevTable)

def writeGem(file, blockName, gemName):
	file.write("{\n\t\"type\": \"minecraft:block\",\n\t\"pools\": [\n\t\t{\n\t\t\t\"rolls\": 1,\n\t\t\t\"entries\": [\n\t\t\t\t{\n\t\t\t\t\t\"type\": \"minecraft:alternatives\",\n\t\t\t\t\t\"children\": [\n\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\"type\": \"minecraft:item\",\n\t\t\t\t\t\t\t\"conditions\": [\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"condition\": \"minecraft:match_tool\",\n\t\t\t\t\t\t\t\t\t\"predicate\": {\n\t\t\t\t\t\t\t\t\t\t\"enchantments\": [\n\t\t\t\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\t\t\t\"enchantment\": \"minecraft:silk_touch\",\n\t\t\t\t\t\t\t\t\t\t\t\t\"levels\": {\n\t\t\t\t\t\t\t\t\t\t\t\t\t\"min\": 1\n\t\t\t\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t\t\t]\n\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t],\n\t\t\t\t\t\t\t\"name\": \"")
	file.write(blockName)
	file.write("\"\n\t\t\t\t\t\t},\n\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\"type\": \"minecraft:item\",\n\t\t\t\t\t\t\t\"functions\": [\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"function\": \"minecraft:apply_bonus\",\n\t\t\t\t\t\t\t\t\t\"enchantment\": \"minecraft:fortune\",\n\t\t\t\t\t\t\t\t\t\"formula\": \"minecraft:ore_drops\"\n\t\t\t\t\t\t\t\t},\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"function\": \"minecraft:explosion_decay\"\n\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t],\n\t\t\t\t\t\t\t\"name\": \"")
	file.write(gemName)
	file.write("\"\n\t\t\t\t\t\t}\n\t\t\t\t\t]\n\t\t\t\t}\n\t\t\t]\n\t\t}\n\t]\n}")

for name in regNames:
	filepath = loottablePath + "/" + name
	if "molten_" in name or "liquid_" in name or "distilled_water" in name or "dirty_water" in name or "steam" in name:
		# Fluids don't have loot tables
		continue

	with open(filepath, "w+") as f:
		if name.startswith("stamp_mill_top") or name.startswith("light_cluster") or name.startswith("stamp_mill_top") or "large_gear_" in name or "mechanism" in name or "reactive_spot" in name:
			# Stamp mill tops and light clusters drop nothing; gear drops handled by TE
			f.write("{\n\t\"type\": \"minecraft:block\",\n\t\"pools\": [\n\t\t{\n\t\t\t\"rolls\": 1,\n\t\t\t\"entries\": [\n\t\t\t\t\n\t\t\t],\n\t\t\t\"conditions\": [\n\t\t\t\t{\n\t\t\t\t\t\"condition\": \"minecraft:survives_explosion\"\n\t\t\t\t}\n\t\t\t]\n\t\t}\n\t]\n}")
		elif name.startswith("ore_void"):
			writeGem(f, "crossroads:ore_void", "crossroads:void_crystal")
		elif name.startswith("ore_ruby"):
			writeGem(f, "crossroads:ore_ruby", "crossroads:gem_ruby")
		elif name.startswith("redstone_crystal"):
			# Redstone crystal drops 1-4 redstone dust (silk touch & fortune applies)
			f.write("{\n\t\"type\": \"minecraft:block\",\n\t\"pools\": [\n\t\t{\n\t\t\t\"rolls\": 1,\n\t\t\t\"entries\": [\n\t\t\t\t{\n\t\t\t\t\t\"type\": \"minecraft:alternatives\",\n\t\t\t\t\t\"children\": [\n\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\"type\": \"minecraft:item\",\n\t\t\t\t\t\t\t\"conditions\": [\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"condition\": \"minecraft:match_tool\",\n\t\t\t\t\t\t\t\t\t\"predicate\": {\n\t\t\t\t\t\t\t\t\t\t\"enchantments\": [\n\t\t\t\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\t\t\t\"enchantment\": \"minecraft:silk_touch\",\n\t\t\t\t\t\t\t\t\t\t\t\t\"levels\": {\n\t\t\t\t\t\t\t\t\t\t\t\t\t\"min\": 1\n\t\t\t\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t\t\t]\n\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t],\n\t\t\t\t\t\t\t\"name\": \"crossroads:redstone_crystal\"\n\t\t\t\t\t\t},\n\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\"type\": \"minecraft:item\",\n\t\t\t\t\t\t\t\"functions\": [\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"function\": \"minecraft:set_count\",\n\t\t\t\t\t\t\t\t\t\"count\": {\n\t\t\t\t\t\t\t\t\t\t\"min\": 2.0,\n\t\t\t\t\t\t\t\t\t\t\"max\": 4.0,\n\t\t\t\t\t\t\t\t\t\t\"type\": \"minecraft:uniform\"\n\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t},\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"function\": \"minecraft:apply_bonus\",\n\t\t\t\t\t\t\t\t\t\"enchantment\": \"minecraft:fortune\",\n\t\t\t\t\t\t\t\t\t\"formula\": \"minecraft:uniform_bonus_count\",\n\t\t\t\t\t\t\t\t\t\"parameters\": {\n\t\t\t\t\t\t\t\t\t\t\"bonusMultiplier\": 1\n\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t},\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"function\": \"minecraft:limit_count\",\n\t\t\t\t\t\t\t\t\t\"limit\": {\n\t\t\t\t\t\t\t\t\t\t\"max\": 4,\n\t\t\t\t\t\t\t\t\t\t\"min\": 1\n\t\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t\t},\n\t\t\t\t\t\t\t\t{\n\t\t\t\t\t\t\t\t\t\"function\": \"minecraft:explosion_decay\"\n\t\t\t\t\t\t\t\t}\n\t\t\t\t\t\t\t],\n\t\t\t\t\t\t\t\"name\": \"minecraft:redstone\"\n\t\t\t\t\t\t}\n\t\t\t\t\t]\n\t\t\t\t}\n\t\t\t]\n\t\t}\n\t]\n}")
		else:
			f.write("{\n\t\"type\": \"minecraft:block\",\n\t\"pools\": [\n\t\t{\n\t\t\t\"rolls\": 1,\n\t\t\t\"entries\": [\n\t\t\t\t{\n\t\t\t\t\t\"type\": \"minecraft:item\",\n\t\t\t\t\t\"name\": \"crossroads:" + name.replace(".json", "", 1) + "\"\n\t\t\t\t}\n\t\t\t],\n\t\t\t\"conditions\": [\n\t\t\t\t{\n\t\t\t\t\t\"condition\": \"minecraft:survives_explosion\"\n\t\t\t\t}\n\t\t\t]\n\t\t}\n\t]\n}")

		f.close()
