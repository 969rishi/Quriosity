package com.quriosity.quriosity.models;

/**
 * Created by WORLD-PC on 5/22/2016.
 */
public class VenderLIstModel
{
    private String coverurl;

    private String imageurl;

    private String iconurl;

    private String vendortypename;

    private String description;

    private String vtid;

    private String vendortypedispname;

    public String getCoverurl ()
    {
        return coverurl;
    }

    public void setCoverurl (String coverurl)
    {
        this.coverurl = coverurl;
    }

    public String getImageurl ()
    {
        return imageurl;
    }

    public void setImageurl (String imageurl)
    {
        this.imageurl = imageurl;
    }

    public String getIconurl ()
    {
        return iconurl;
    }

    public void setIconurl (String iconurl)
    {
        this.iconurl = iconurl;
    }

    public String getVendortypename ()
    {
        return vendortypename;
    }

    public void setVendortypename (String vendortypename)
    {
        this.vendortypename = vendortypename;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getVtid ()
    {
        return vtid;
    }

    public void setVtid (String vtid)
    {
        this.vtid = vtid;
    }

    public String getVendortypedispname ()
    {
        return vendortypedispname;
    }

    public void setVendortypedispname (String vendortypedispname)
    {
        this.vendortypedispname = vendortypedispname;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [coverurl = "+coverurl+", imageurl = "+imageurl+", iconurl = "+iconurl+", vendortypename = "+vendortypename+", description = "+description+", vtid = "+vtid+", vendortypedispname = "+vendortypedispname+"]";
    }
}
