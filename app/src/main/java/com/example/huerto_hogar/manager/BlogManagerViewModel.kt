package com.example.huerto_hogar.manager

import androidx.lifecycle.ViewModel
import com.example.huerto_hogar.R
import com.example.huerto_hogar.model.Blog


class BlogManagerViewModel : ViewModel() {
    private val listBlogs = listOf(
        Blog(
            title = "La importancia de consumir productos locales y frescos",
            bannerImg = R.drawable.blog1,
            summary = "Descubre por qué elegir frutas y verduras locales no solo beneficia tu salud, sino también a los agricultores y al medio ambiente.",
            bodyText = listOf(
                "En un mundo donde muchas veces priorizamos la rapidez sobre la calidad, olvidamos el verdadero valor de los alimentos frescos y locales. En Huerto Hogar creemos que cada compra es una decisión con impacto: al elegir productos directamente del campo, no solo recibes frutas y verduras llenas de sabor y nutrientes, también apoyas a familias agricultoras de distintas regiones de Chile.",
                "Además, consumir productos locales reduce la huella de carbono, ya que los alimentos no necesitan recorrer grandes distancias para llegar a tu mesa. Esto significa menos transporte, menos contaminación y más frescura.",
                "Imagina preparar un jugo con naranjas recién cosechadas o un batido verde con espinacas orgánicas cultivadas sin químicos. Esa frescura se siente, se saborea y también contribuye a un estilo de vida más sostenible.",
                "En Huerto Hogar te invitamos a ser parte de este cambio: cuidar tu salud, apoyar al campo y construir un futuro más verde con cada compra."
            ),
            authorImg = R.drawable.pf,
            authorName = "Freddy Turbina",
            publishDate = "02 de Septiembre, 2025",
            tags = listOf("Sostenibilidad", "Huella de carbono")
        ),
        Blog(
            title = "Cómo armar una despensa saludable con productos de Huerto Hogar",
            bannerImg = R.drawable.blog2,
            summary = "Aprende a organizar tu despensa con productos frescos y orgánicos que te ayudarán a llevar una alimentación balanceada y consciente.",
            bodyText = listOf(
                "Una despensa bien organizada es el primer paso hacia una vida más saludable. En Huerto Hogar sabemos que la base de una buena alimentación está en los ingredientes, por eso queremos inspirarte a llenar tu cocina con lo mejor del campo.",
                "Empieza con frutas frescas como manzanas Fuji y plátanos Cavendish, ideales para snacks rápidos o postres nutritivos. Agrega verduras orgánicas como zanahorias o pimientos tricolores, que aportan color, vitaminas y antioxidantes a tus comidas.",
                "No olvides los básicos que no pueden faltar: miel orgánica para endulzar naturalmente tus bebidas, y quinua para preparar ensaladas o acompañar platos principales. Estos productos, además de ser nutritivos, están elaborados bajo prácticas sostenibles que respetan la tierra y a quienes la trabajan.",
                "Con pequeñas elecciones diarias puedes transformar tu alimentación y la de tu familia. Y lo mejor: todo lo encuentras en un solo lugar, con la frescura y calidad que Huerto Hogar garantiza."
            ),
            authorImg = R.drawable.sv,
            authorName = "Dante Guantecito",
            publishDate = "20 de Abril, 2025",
            tags = listOf("Despensa saludable", "Organizacion cocina")
        )
    )

    fun getBlogs(): List<Blog> = listBlogs
}