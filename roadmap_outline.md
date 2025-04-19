# implementation strategy

Okay, let's outline a strategy for implementing your "Mindful Aftertaste" mod for Fabric 1.21.5. This concept of shifting buffs based *only* on the last meal is an interesting twist on food mechanics.

Here’s a breakdown of the implementation strategy:

**1. Recommended Framework & APIs (Fabric 1.21.5)**

- **Fabric API:** This is the foundation. You'll need modules like fabric-api-base, fabric-object-builder-api-v1, fabric-lifecycle-events-v1, potentially fabric-command-api-v2 (for debugging/testing), and crucially, fabric-convention-tags-v1 and fabric-components-v0 (Cardinal Components).
- **Mixins:** You'll heavily rely on Mixins to inject logic into vanilla Minecraft methods, specifically for detecting food consumption.
- **Cardinal Components API:** This Fabric API module is the standard and recommended way to attach persistent custom data (like the last eaten food category) to entities like players.
- **Minecraft Data Tags:** Essential for categorizing food items in a flexible and compatible way.

**2. Food Categorization (Data Tags)**

- **Strategy:** Use Minecraft's built-in Data Tag system. This is the most vanilla-friendly and compatible approach.
- **Implementation:**
- Define custom TagKey<Item> instances for each of your five categories (Fruits, Vegetables, Proteins, Grains, Sugars) within your mod's namespace (e.g., mindfulaftertaste:foods/fruits, mindfulaftertaste:foods/vegetables, etc.).
- Create corresponding JSON files in your src/main/resources/data/mindfulaftertaste/tags/items/foods/ directory (e.g., fruits.json, vegetables.json).
- Populate these tag files with vanilla food item IDs initially (e.g., minecraft:apple in fruits.json).
- **Compatibility:** This approach is excellent for compatibility. Other mods or data packs can easily add their food items to your tags by providing their own tag files under the data/<othermod>/tags/items/mindfulaftertaste/foods/ path, merging the entries. You can also add tags from other mods (e.g., a common c:fruits tag if using Fabric Conventional Tags) to your category tags to automatically include items.

**3. Event Detection (Mixin)**

- **Strategy:** Inject code into the method responsible for handling food consumption using a Mixin.
- **Implementation:**
- Create a Mixin targeting net.minecraft.entity.LivingEntity.
- Inject into the eatFood(World world, ItemStack stack) method (verify the exact signature for 1.21.5, it might involve FoodComponent as well). A @Inject annotation with at = @At("RETURN") is suitable, allowing you to react *after* the food has been successfully consumed.
- Inside your injected method, check if the LivingEntity is a ServerPlayerEntity. All buff logic should run server-side.
- Get the consumed ItemStack.
- Check if the ItemStack's item belongs to any of your defined food category tags.

**4. State Management (Cardinal Components)**

- **Strategy:** Use the Cardinal Components API to attach data directly to the PlayerEntity.
- **Implementation:**
- Define a Component Interface (e.g., ILastEatenFoodComponent) with methods like setLastCategory(Identifier categoryTagId) and getLastCategory(). You might also add methods to track the expiry time if needed, although StatusEffectInstance handles duration itself.
- Create an implementation class for this interface.
- Register your component for PlayerEntity objects during your mod's initialization phase using ComponentRegistryV3.INSTANCE.getOrCreate(YOUR_COMPONENT_ID, ILastEatenFoodComponent.class).
- In your LivingEntity.eatFood mixin, retrieve the component instance from the ServerPlayerEntity using YOUR_COMPONENT_KEY.get(player) and update the last eaten category.

**5. Buff Mapping & Effect Application**

- **Strategy:** Map your food category tags to specific StatusEffect instances and apply them to the player, removing any previous buff from *this mod*.
- **Implementation:**
- Define which StatusEffect (vanilla or custom) corresponds to each food category tag. You can use a Map<TagKey<Item>, StatusEffect> or similar structure.
- In your LivingEntity.eatFood mixin, after identifying the consumed food's category:

1. Retrieve the player's ILastEatenFoodComponent.
2. Identify the StatusEffect associated with the *previously* stored category in the component (if any).
3. If a previous Mindful Aftertaste buff exists, remove it from the player using player.removeStatusEffect(previousEffect). You might need a way to identify which active effects are specifically from your mod (e.g., store the active effect type in the component, or check against your known list of mapped effects).
4. Identify the StatusEffect associated with the *newly* consumed category.
5. Apply the new effect using player.addStatusEffect(new StatusEffectInstance(newEffect, durationTicks, amplifier, ambient, showParticles, showIcon)). Configure duration and amplifier as needed for balance.
6. Update the player's component with the new category tag ID.

**6. Potential Challenges & Considerations**

- **Mod Compatibility (Food Items):** Relying on tags is the best approach, but requires items to be tagged correctly. Un-tagged foods won't grant buffs. Consider providing configuration options or commands for users to manually assign un-tagged items if needed, though encouraging tag usage is preferred.
- **Buff Duration/Strength:** Balancing the duration and power of each buff is crucial for gameplay. Make these configurable if possible.
- **Interaction with Other Effects:** Decide how your buffs interact with vanilla/modded effects. The current plan replaces only *your mod's* previous buff, allowing others (like beacon effects) to coexist. Be clear about this interaction.
- **Milk:** Drinking milk clears all status effects. Your system needs to handle this gracefully. When a buff is cleared by milk, the component state ideally shouldn't change (the *last eaten* food remains the same), but the player won't have the buff until they eat again. Your mixin logic correctly reapplies the buff on the next categorized food consumption.
- **Non-Food Consumption:** Ensure your mixin correctly identifies actual food items and doesn't trigger on potions or other consumable ItemStacks unless intended. Check item.isFood() and your tags.
- **Feedback:** Provide clear feedback to the player (e.g., a brief chat message, sound effect, or distinct particle effect) when a buff is applied or replaced. The standard status effect icon helps, but extra feedback reinforces the mechanic.
- **Server-Side Logic:** All state changes and effect applications *must* happen on the server (ServerPlayerEntity) to prevent cheating and ensure consistency in multiplayer. Cardinal Components and status effect synchronization handle much of the networking implicitly.

This strategy provides a solid foundation using standard Fabric practices. Remember to start simple, perhaps implementing one category and buff first, then expanding
