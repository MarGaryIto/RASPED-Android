package com.example.adminprospera.rasped_tool;

enum AdministradorModelObject {

    personal(R.string.st_personal, R.layout.frag_ad_personal),
    horarios(R.string.st_horarios, R.layout.frag_ad_horarios),
    puestos(R.string.st_puestos, R.layout.frag_ad_puestos);

    private int mTitleResId;
    private int mLayoutResId;

    AdministradorModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
