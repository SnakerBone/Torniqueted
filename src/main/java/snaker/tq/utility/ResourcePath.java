package snaker.tq.utility;

import net.minecraft.resources.ResourceLocation;
import snaker.snakerlib.SnakerLib;
import snaker.tq.Tourniqueted;

/**
 * Created by SnakerBone on 15/02/2023
 **/
public class ResourcePath extends ResourceLocation
{
    public static final ResourcePath SOLID_TEXTURE = new ResourcePath("textures/solid.png");
    public static final ResourcePath NO_TEXTURE = new ResourcePath("textures/clear.png");

    public static <T> ResourcePath fromClass(Class<T> clazz)
    {
        return new ResourcePath(clazz);
    }

    public ResourcePath(String path)
    {
        super(Tourniqueted.MODID, path);
    }

    private <T> ResourcePath(Class<T> clazz)
    {
        super(Tourniqueted.MODID, SnakerLib.i18nf(clazz.getSimpleName()));
    }
}
