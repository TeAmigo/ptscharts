// <2011-08-05 Fri 19:06> ~/.groovy/groovysh.rc is where the goodies are defined
syms = {syminfs.getSymInfos().each{println it}}


//Currencies
audh = audhh()
audd = auddd()

cadh=cadhh()
cadd=cadd()

dxh=dxhh()
dxd=dxdd()

dx5=dx5m()

eurh=eurhh()
eurd=eurdd()

gbph=gbphh()
gbpd=gbpdd()

//S&P 500
esh=eshh()
esd=esdd()


//10yr notes
znm = znmm()
znh = znhh()
znd = zndd()

//5yr notes
zfh = zfhh()

//30yr
zbh = zbhh()
zbd = zbdd()

//Gold
gchh = {IndicatorSet1.run(factory, "GC",new DateTime().minusMonths(1).toString(), new DateTime().toString(), 60);}
gcdd = {IndicatorSet1.run(factory, "GC","2009-03-11T00:00", new DateTime().plusDays(1).toString(), 60 * 24);}
gch = gchh()
gcd = gcdd()

//Corn
zch = zchh()
zcd = zcdd()

//Soy
zsh = zshh()
zsd = zsdd()

//Wheat
zwh = zwhh()
zwd = zwdd()

//oil
clh = clhh()
cld = cldd()
